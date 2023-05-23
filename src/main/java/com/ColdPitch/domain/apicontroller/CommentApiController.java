package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.comment.CommentState;
import com.ColdPitch.domain.entity.dto.comment.CommentRequestDto;
import com.ColdPitch.domain.entity.dto.comment.CommentResponseDto;
import com.ColdPitch.domain.repository.CommentRepository;
import com.ColdPitch.domain.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Transactional
public class CommentApiController {
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @GetMapping("/comment")
    @Transactional(readOnly = true)
    public ResponseEntity<List<CommentResponseDto>> getCommentList(Long id, String type) {
        if (id == null && type == null) {
            return ResponseEntity.ok(commentService.findAll());
        }

        switch (type) {
            case "postId" :
                return ResponseEntity.ok(commentService.findCommentsByPostId(id));
            case "commentId" :
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
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(Collections.singletonList(
                    commentService.findCommentsByCommentId(id)));
            default:
                break;
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentResponseDto> postComment(CommentRequestDto requestDto) {
        if (requestDto == null) {
            return ResponseEntity.badRequest().build();
        }

        CommentResponseDto responseDto = commentService.saveCommentRequestDto(requestDto);

        if (responseDto == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/comment")
    public ResponseEntity<CommentResponseDto> patchComment(CommentRequestDto requestDto) {
        if (requestDto == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
            commentService.updateComment(requestDto.getId(), requestDto.getText()));
    }

    @DeleteMapping("/comment")
    public ResponseEntity<CommentResponseDto> deleteComment(Long commentId) {
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

    @GetMapping("/comment/user")
    public ResponseEntity<List<CommentResponseDto>> getUserComment(Long userId) {
        return ResponseEntity.ok(commentService.findCommentsByUserId(userId));
    }

    @PostMapping("/comment/dummy")
    @Operation(summary = "Creating Dummy Comment which given amount", description = "Dummy Comment")
    public ResponseEntity<List<CommentResponseDto>> postDummyComment(int amount) {
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
