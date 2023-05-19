package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.repository.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> findCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public Comment save(Comment comment) {
        return commentRepository.saveAndFlush(comment);
    }

    public Comment updateComment(Long id, String updatedText) {
        Comment entity = commentRepository.findById(id).orElse(null);

        entity.setText(updatedText);

        return commentRepository.saveAndFlush(entity);
    }

    public String deleteComment(Long id) {
        Comment entity = commentRepository.findById(id).orElse(null);

        if (entity == null) {
            return "comment was not found";
        }

        commentRepository.delete(entity);

        return "comment delete successful";
    }

    public List<Comment> findCommentsByParentId(Long parentId) {
        return commentRepository.findAllByParentId(parentId);
    }

    public List<Comment> findCommentsByUserId(Long userId) {
        return commentRepository.findAllByUserId(userId);
    }
}
