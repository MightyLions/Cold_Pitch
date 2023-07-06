package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.entity.QComment;
import com.ColdPitch.domain.entity.QPost;
import com.ColdPitch.domain.entity.post.PostState;
import com.ColdPitch.domain.repository.support.Querydsl4RepositorySupport;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.ColdPitch.domain.entity.QComment.*;
import static com.ColdPitch.domain.entity.QPost.*;

@Repository
@Slf4j
@Transactional(readOnly = true)
public class PostRepositoryImpl extends Querydsl4RepositorySupport implements PostRepositoryCustom {
    public PostRepositoryImpl() {
        super(Post.class);
    }

    @Override
    public Optional<Post> findByIdForUser(Long postId) {
        return selectFrom(post)
            .where(post.id.eq(postId))
            .where(post.status.ne(PostState.DELETED))
            .leftJoin(post.comments, comment)
            .fetchJoin()
            .distinct()
            .fetch()
            .stream().findFirst();
    }

    @Override
    public Optional<Post> findByIdForAdmin(Long postId) {
        return selectFrom(post)
            .where(post.id.eq(postId))
            .leftJoin(post.comments, comment)
            .fetchJoin()
            .distinct()
            .stream().findFirst();
    }

    @Override
    public List<Post> findAllForUser(Long userId) {
        List<Post> postWhereOpened =  selectFrom(post)
            .where(post.status.eq(PostState.OPEN))
            .leftJoin(post.comments, comment)
            .fetchJoin()
            .distinct()
            .fetch();

        List<PostState> privateOrClosed = Arrays.asList(PostState.CLOSED, PostState.PRIVATE);
        List<Post> postWherePrivateOrClosed = selectFrom(post)
            .where(post.user.id.eq(userId))
            .where(post.status.in(privateOrClosed))
            .leftJoin(post.comments, comment)
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
        return selectFrom(post)
            .leftJoin(post.comments, comment)
            .fetchJoin()
            .distinct()
            .fetch();
    }
}
