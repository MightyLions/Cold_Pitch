package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.entity.QPost;
import com.ColdPitch.domain.entity.post.PostState;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findAllForUser(Long userId) {
        List<Post> postWhereOpened =  queryFactory
            .selectFrom(QPost.post)
            .where(QPost.post.status.eq(PostState.OPEN))
            .fetch();

        List<PostState> privateOrClosed = Arrays.asList(PostState.CLOSED, PostState.PRIVATE);
        List<Post> postWherePrivateOrClosed = queryFactory
            .selectFrom(QPost.post)
            .where(QPost.post.user.id.eq(userId))
            .where(QPost.post.status.in(privateOrClosed))
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
            .fetch();
    }
}
