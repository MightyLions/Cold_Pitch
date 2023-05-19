package com.ColdPitch.domain.apicontroller.comment;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.repository.CommentRepository;
import com.ColdPitch.domain.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentApiController {
    private final CommentService comService;
    private final CommentRepository commentRepository;

    @GetMapping("/comment")
    public List<Comment> getCommentList(Long postId) {
        List<Comment> list = comService.findCommentsByPostId(postId);
        return list;
    }

    @PostMapping("/comment")
    public Comment postComment(Comment comment) {
        Comment com = comment;

        return comService.save(com);
    }

    @PatchMapping("/comment")
    public Comment patchComment(Comment comment) {
        return comService.updateComment(comment.getId(), comment.getText());
    }

    @DeleteMapping("/comment")
    public String deleteComment(Long commentId) {
        return comService.deleteComment(commentId);
    }

    @GetMapping("/comment/reply")
    public List<Comment> getReplyComments(Long parentId) {
        return comService.findCommentsByParentId(parentId);
    }

    @GetMapping("/comment/user")
    public List<Comment> getUserComment(Long userId) {
        return comService.findCommentsByUserId(userId);
    }

    @PostMapping("/comment/dummy")
    @Operation(summary = "Creating Dummy Comment which given amount", description = "Dummy Comment")
    public List<Comment> postDummyComment(int amount) {
        List<Comment> list = new ArrayList<>();

        for (long i = 0; i < amount; i++) {
            Comment comment = Comment.builder()
                .userId(i)
                .text("dummy comment " + i)
                .postId((long) (1.0 - Math.random()) * amount)
                .pCommentId(i)
                .createdBy("dummy")
                .modifiedBy("dummy")
                .build();

            if (i == 0) {
                comment = comment.toBuilder()
                    .postId(1L)
                    .build();
            }

            if (i % 2 == 0) {
                comment = comment.toBuilder()
                    .userId(1L)
                    .build();
            }
            list.add(comment);
        }
        list = commentRepository.saveAllAndFlush(list);

        return list;
    }
}
