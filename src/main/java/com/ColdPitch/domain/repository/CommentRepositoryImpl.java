package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.QComment;
import com.ColdPitch.domain.entity.comment.CommentState;
import com.ColdPitch.domain.repository.support.Querydsl4RepositorySupport;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.ColdPitch.domain.entity.QComment.*;

@Repository
@Transactional
@Slf4j
public class CommentRepositoryImpl extends Querydsl4RepositorySupport implements CommentRepositoryCustom {
    public CommentRepositoryImpl() {
        super(Comment.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByPostId(Long postId) {
        return selectFrom(comment)
            .where(comment.postId.eq(postId))
            .where(comment.status.ne(CommentState.DELETED))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByPostIdForAdmin(Long postId) {
        return selectFrom(comment)
            .where(comment.postId.eq(postId))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByParentId(Long pCommentId) {
        return selectFrom(comment)
            .where(comment.pId.eq(pCommentId))
            .where(comment.status.ne(CommentState.DELETED))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByParentIdForAdmin(Long pCommentId) {
        return selectFrom(comment)
            .where(comment.pId.eq(pCommentId))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByUserId(Long userId) {
        return selectFrom(comment)
            .where(comment.userId.eq(userId))
            .where(comment.status.ne(CommentState.DELETED))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByUserIdForAdmin(Long userId) {
        return selectFrom(comment)
            .where(comment.userId.eq(userId))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllForUser() {
        return selectFrom(QComment.comment)
            .where(QComment.comment.status.ne(CommentState.DELETED))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllForAdmin() {
        return selectFrom(QComment.comment)
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findByIdForUser(Long id) {
        return selectFrom(comment)
            .where(comment.id.eq(id))
            .where(comment.status.ne(CommentState.DELETED))
            .fetch()
            .stream()
            .findAny()
            .orElse(null);
    }
}
