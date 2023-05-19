package com.ColdPitch.domain.apicontroller.comment;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.service.CommentService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
}
