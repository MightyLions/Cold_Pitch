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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Transactional
public class CommentApiController {
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @GetMapping("/comment")
    @Transactional(readOnly = true)
    @Operation(summary = "댓글을 조회하는 GET 메소드", description = "주어진 type과 id를 바탕으로 댓글을 조회")
    public ResponseEntity<List<CommentResponseDto>> getCommentList(
        @ApiParam(value="댓글 id")
        @RequestParam(name = "CommentId", required = false) Long id,
        @ApiParam(value="결과 요청 타입")
        @RequestParam(name = "CommentRequestType", required = true) CommentRequestType type) {
        switch (type) {
            case POST_ID :
                return ResponseEntity.ok(commentService.findListByPostId(id));
            case COMMENT_ID :
                if (id == null) {
                    return ResponseEntity.ok(commentRepository
                        .findAll()
                        .stream()
                        .filter(comment -> !comment.getStatus().equals(CommentState.DELETED))
                        .map(CommentService::commentToResponseDto)
                        .collect(Collectors.toList())
                    );
                }

                if (!commentRepository.existsById(id)) {
                    throw new CustomException(ErrorCode.COMMENT_BAD_REQUEST);
                }
                return ResponseEntity.ok(Collections.singletonList(
                    commentService.findCommentsByCommentId(id)));
            case USER_ID:
                return ResponseEntity.ok(commentService.findCommentsByUserId(id));
            case ALL:
                return ResponseEntity.ok(commentService.findAll());
            default:
                break;
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/comment")
    @Operation(summary = "댓글을 등록하는 POST 메소드", description = "댓글 요청 DTO를 바탕으로 댓글을 등록하는 메소드")
    public ResponseEntity<CommentResponseDto> postComment(
            @Valid
            @RequestBody
            CommentRequestDto requestDto
    ) {
        if (requestDto == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!SecurityUtil.checkCurrentUserRole("ADMIN")) {
            if (!userRepository.existsById(requestDto.getUserId())) {
                throw new CustomException(ErrorCode.USER_NOT_FOUND);
            }
            UserResponseDto userResponseDto = UserResponseDto
                .of(userRepository.findByEmail
                        (SecurityUtil.getCurrentUserEmail().orElse(null))
                    .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
            if (!userResponseDto.getId().equals(requestDto.getUserId())) {
                throw new CustomException(ErrorCode.USER_NOT_MATCH);
            }
        }

        CommentResponseDto responseDto = commentService.saveCommentRequestDto(requestDto);

        if (responseDto == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/comment")
    @Operation(summary = "댓글을 수정하는 PATCH 메소드", description = "댓글 요청 DTO를 바탕으로 댓글을 수정하는 메소드")
    public ResponseEntity<CommentResponseDto> patchComment(
            @Valid
            @RequestBody
            CommentRequestDto requestDto
    ) {
        if (requestDto == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!SecurityUtil.checkCurrentUserRole("ADMIN")) {
            if (!userRepository.existsById(requestDto.getUserId())) {
                throw new CustomException(ErrorCode.USER_NOT_FOUND);
            }
            UserResponseDto userResponseDto = UserResponseDto
                .of(userRepository.findByEmail
                        (SecurityUtil.getCurrentUserEmail().orElse(null))
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_AUTHOR_NOT_MATCH)));
            if (!userResponseDto.getId().equals(requestDto.getUserId())) {
                throw new CustomException(ErrorCode.USER_NOT_MATCH);
            }
        }

        return ResponseEntity.ok(
            commentService.updateComment(requestDto.getId(), requestDto.getText()));
    }

    @DeleteMapping("/comment")
    @Operation(summary = "댓글을 삭제하는 DELETE 메소드", description = "댓글 id를 기반으로 댓글 상태를 DELETED로 변경")
    public ResponseEntity<CommentResponseDto> deleteComment(
        @ApiParam(name = "댓글 id")
        @RequestParam(name = "CommentId", required = true) Long commentId
    ) {
        if (!commentRepository.existsById(commentId)) {
            return ResponseEntity.noContent().build();
        }

        commentService.changeState(commentId, CommentState.DELETED);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/comment/reply")
    public ResponseEntity<List<CommentResponseDto>> getReplyComments(Long parentId) {
        return ResponseEntity.ok(commentService.findCommentsByParentId(parentId));
    }

    @PostMapping("/comment/dummy")
    @Operation(summary = "더미 댓글을 생성 메소드", description = "주어진 갯수만큼 더미 댓글을 생성하는 메소드")
    public ResponseEntity<List<CommentResponseDto>> postDummyComment(
        @ApiParam("생성 갯수")
        @RequestParam(name="amount", defaultValue = "10") int amount
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
