package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.comment.CommentRequestType;
import com.ColdPitch.domain.entity.comment.CommentState;
import com.ColdPitch.domain.entity.dto.comment.CommentRequestDto;
import com.ColdPitch.domain.entity.dto.comment.CommentResponseDto;
import com.ColdPitch.domain.entity.dto.user.UserResponseDto;
import com.ColdPitch.domain.repository.CommentRepository;
import com.ColdPitch.domain.repository.UserRepository;
import com.ColdPitch.domain.service.CommentService;
import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.handler.ErrorCode;
import com.ColdPitch.utils.SecurityUtil;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
@Transactional
public class CommentApiController {
    private final CommentService commentService;
    //  TODO 나중에 CustomUserDetails가 추가될때 지우기
    private final UserRepository userRepository;

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "댓글을 조회하는 GET 메소드", description = "주어진 type과 id를 바탕으로 댓글을 조회")
    public ResponseEntity<List<CommentResponseDto>> getCommentList(
            @ApiParam(value = "댓글 id")
            @RequestParam(name = "CommentId", required = false) Long id,
            @ApiParam(value = "결과 요청 타입")
            @RequestParam(name = "CommentRequestType") CommentRequestType type) {
        switch (type) {
            case POST_ID:
                return ResponseEntity.ok(commentService.findListByPostId(id));
            case COMMENT_ID:
                if (id == null) {
                    return ResponseEntity.ok(commentService.findAll());
                }

                return ResponseEntity.ok(Collections
                        .singletonList(commentService.findCommentById(id)));
            case USER_ID:
                return ResponseEntity.ok(commentService.findCommentsByUserId(id));
            case ALL:
                return ResponseEntity.ok(commentService.findAll());
            default:
                break;
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    @Operation(summary = "댓글을 등록하는 POST 메소드", description = "댓글 요청 DTO를 바탕으로 댓글을 등록하는 메소드")
    public ResponseEntity<CommentResponseDto> postComment(
            @Valid
            @RequestBody
            CommentRequestDto requestDto
    ) {
        if (requestDto == null) {
            return ResponseEntity.badRequest().build();
        }

        //  TODO 추후에 CustomUserDetails를 추가해서 제거할 코드
        //  현재 댓글에서의 userId와 현재 로그인한 유저의 id를 비교
        if (!SecurityUtil.checkCurrentUserRole("ADMIN")) {
            UserResponseDto userResponseDto = UserResponseDto
                .fromEntity(userRepository.findByEmail
                        (SecurityUtil.getCurrentUserEmail().orElse(null))
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
            if (!userResponseDto.getId().equals(requestDto.getUserId())) {
                throw new CustomException(ErrorCode.USER_CURRENT_USER_REQUEST_USER_NOT_MATCH);
            }
        }

        return ResponseEntity.ok(commentService.saveCommentRequestDto(requestDto));
    }

    @PatchMapping
    @Operation(summary = "댓글을 수정하는 PATCH 메소드", description = "댓글 요청 DTO를 바탕으로 댓글을 수정하는 메소드")
    public ResponseEntity<CommentResponseDto> patchComment(
            @Valid
            @RequestBody
            CommentRequestDto requestDto
    ) {
        if (requestDto == null) {
            return ResponseEntity.badRequest().build();
        }

        //  TODO 추후에 CustomUserDetails를 추가해서 제거할 코드
        if (!SecurityUtil.checkCurrentUserRole("ADMIN")) {

            UserResponseDto userResponseDto = UserResponseDto
                    .fromEntity(userRepository.findByEmail
                                    (SecurityUtil.getCurrentUserEmail().orElse(null))
                            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_AUTHOR_NOT_MATCH)));
            if (!userResponseDto.getId().equals(requestDto.getUserId())) {
                throw new CustomException(ErrorCode.USER_NOT_MATCH);
            }
        }

        return ResponseEntity.ok(
                commentService.updateComment(requestDto.getId(), requestDto.getText()));
    }

    @DeleteMapping
    @Operation(summary = "댓글을 삭제하는 DELETE 메소드", description = "댓글 id를 기반으로 댓글 상태를 DELETED로 변경")
    public ResponseEntity<CommentResponseDto> deleteComment(
            @ApiParam(name = "댓글 id")
            @RequestParam(name = "CommentId")
            Long commentId
    ) {
        if (!commentService.changeState(commentId, CommentState.DELETED)) {
            ResponseEntity.internalServerError().build();
        };

        return ResponseEntity.ok().build();
    }

    @GetMapping("/reply")
    @Operation(summary = "댓글을 검색하는 GET 메소드", description = "부모 댓글 id를 기반으로 검색하는 GET 메소드")
    public ResponseEntity<List<CommentResponseDto>> getReplyComments(
            @ApiParam(name = "부모 댓글 id")
            @RequestParam(name = "ParentId")
            Long parentId
    ) {
        return ResponseEntity.ok(commentService.findCommentsByParentId(parentId));
    }

    //  TODO DEBUG 디버깅용 메소드
    //  나중에 지우기
    @PostMapping("/dummy")
    @Operation(summary = "더미 댓글을 생성 메소드", description = "주어진 갯수만큼 더미 댓글을 생성하는 메소드")
    public ResponseEntity<List<CommentResponseDto>> postDummyComment(
            @ApiParam("생성 갯수")
            @RequestParam(name = "Amount", defaultValue = "10") int amount
    ) {
        List<CommentResponseDto> list = new ArrayList<>();

        for (long i = 0; i < amount; i++) {
            CommentRequestDto requestDto = CommentRequestDto.builder()
                    .userId(i)
                    .text("dummy comment " + i)
                    .postId((long) Math.max(1, (1.0 - Math.random()) * Math.max(3, amount / 5)))
                    .pCommentId((long) Math.max(1, (1.0 - Math.random()) * Math.max(3, amount / 5)))
                    .status(CommentState.OPEN)
                    .build();

            if (i == 0) {
                requestDto.setPostId(2L);
            }

            if (i % 2 == 0) {
                requestDto.setUserId(1L);
            }
            list.add(commentService.saveCommentRequestDto(requestDto));
        }

        return ResponseEntity.ok(list);
    }
}
