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

    @GetMapping("/comment/post")
    public List<Comment> getCommentList(HttpServletRequest request) {
        List<Comment> list = comService.findCommentsByPostId(Long.valueOf(request.getParameter("postId")));
        return list;
    }

    @PostMapping("/comment")
    public Comment postComment(HttpServletRequest request) {
        Comment com = Comment.builder()
            .userId(Long.valueOf(request.getParameter("userId")))
            .postId(Long.valueOf(request.getParameter("postId")))
            .text(request.getParameter("text"))
            .pCommentId(request.getParameter("replyComId") != null ?
                Long.valueOf(request.getParameter("replyComId")) : null)
            .build();

        return comService.save(com);
    }

    @PatchMapping("/comment")
    public Comment patchComment(HttpServletRequest request) {
        return comService.updateComment(Long.valueOf(request.getParameter("comId")),
            request.getParameter("text"));
    }

    @DeleteMapping("/comment")
    public String deleteComment(HttpServletRequest request) {
        return comService.deleteComment(Long.valueOf(request.getParameter("comId")));
    }
}
