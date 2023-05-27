package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.entity.QComment;
import com.ColdPitch.domain.entity.QPost;
import com.ColdPitch.domain.entity.post.PostState;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> findByIdForUser(Long postId) {
        return queryFactory
            .selectFrom(QPost.post)
            .where(QPost.post.id.eq(postId))
            .where(QPost.post.status.ne(PostState.DELETED))
            .leftJoin(QPost.post.comments, QComment.comment)
            .fetchJoin()
            .distinct()
            .fetch()
            .stream().findFirst();
    }

    @Override
    public Optional<Post> findByIdForAdmin(Long postId) {
        return queryFactory
            .selectFrom(QPost.post)
            .where(QPost.post.id.eq(postId))
            .leftJoin(QPost.post.comments, QComment.comment)
            .fetchJoin()
            .distinct()
            .stream().findFirst();
    }

    @Override
    public List<Post> findAllForUser(Long userId) {
        List<Post> postWhereOpened =  queryFactory
            .selectFrom(QPost.post)
            .where(QPost.post.status.eq(PostState.OPEN))
            .leftJoin(QPost.post.comments, QComment.comment)
            .fetchJoin()
            .distinct()
            .fetch();

        List<PostState> privateOrClosed = Arrays.asList(PostState.CLOSED, PostState.PRIVATE);
        List<Post> postWherePrivateOrClosed = queryFactory
            .selectFrom(QPost.post)
            .where(QPost.post.user.id.eq(userId))
            .where(QPost.post.status.in(privateOrClosed))
            .leftJoin(QPost.post.comments, QComment.comment)
            .fetchJoin()
            .distinct()
            .fetch();

        List<Post> postsForUser = new ArrayList<>();
        postsForUser.addAll(postWhereOpened);
        postsForUser.addAll(postWherePrivateOrClosed);

        return postsForUser;
    }

    @Override
    public List<Post> findAllForAdmin() {
        return queryFactory
            .selectFrom(QPost.post)
            .leftJoin(QPost.post.comments, QComment.comment)
            .fetchJoin()
            .distinct()
            .fetch();
    }
}
