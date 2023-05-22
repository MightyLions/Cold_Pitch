package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.QComment;
import com.ColdPitch.domain.entity.comment.CommentState;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByPostId(Long postId) {
        return queryFactory
            .selectFrom(QComment.comment)
            .where(QComment.comment.postId.eq(postId))
            .where(QComment.comment.status.ne(CommentState.DELETED))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByPostIdForAdmin(Long postId) {
        return queryFactory
            .selectFrom(QComment.comment)
            .where(QComment.comment.postId.eq(postId))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByParentId(Long pCommentId) {
        return queryFactory
            .selectFrom(QComment.comment)
            .where(QComment.comment.pCommentId.eq(pCommentId))
            .where(QComment.comment.status.ne(CommentState.DELETED))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByParentIdForAdmin(Long pCommentId) {
        return queryFactory
            .selectFrom(QComment.comment)
            .where(QComment.comment.pCommentId.eq(pCommentId))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByUserId(Long userId) {
        return queryFactory
            .selectFrom(QComment.comment)
            .where(QComment.comment.userId.eq(userId))
            .where(QComment.comment.status.ne(CommentState.DELETED))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByUserIdForAdmin(Long userId) {
        return queryFactory
            .selectFrom(QComment.comment)
            .where(QComment.comment.userId.eq(userId))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllForUser() {
        return queryFactory
            .selectFrom(QComment.comment)
            .where(QComment.comment.status.ne(CommentState.DELETED))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllForAdmin() {
        return queryFactory
            .selectFrom(QComment.comment)
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findByIdForUser(Long id) {
        return queryFactory
            .selectFrom(QComment.comment)
            .where(QComment.comment.id.eq(id))
            .where(QComment.comment.status.ne(CommentState.DELETED))
            .fetch()
            .stream()
            .findAny()
            .orElse(null);
    }
}
