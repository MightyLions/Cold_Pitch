package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {
    List<Comment> findAllByPostIdNotIncludingDeleted(Long postId);
    List<Comment> findAllByParentId(Long pCommentId);

    List<Comment> findAllByParentIdNotIncludeDeleted(Long pCommentId);

    List<Comment> findAllByUserIdNotIncludingDeleted(Long userId);

    List<Comment> findAllNotIncludingDeleted();

    Optional<Comment> findByIdNotIncludingDeleted(Long id);
}
