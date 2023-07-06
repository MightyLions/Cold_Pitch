package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Comment;
import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findAllByPostId(Long postId);
    List<Comment> findAllByPostIdForAdmin(Long postId);

    List<Comment> findAllByParentId(Long pCommentId);
    List<Comment> findAllByParentIdForAdmin(Long pCommentId);

    List<Comment> findAllByUserId(Long userId);
    List<Comment> findAllByUserIdForAdmin(Long userId);

    List<Comment> findAllForUser();

    List<Comment> findAllForAdmin();

    Comment findByIdForUser(Long id);
}
