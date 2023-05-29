package com.ColdPitch.domain.repository;

import static com.ColdPitch.utils.RandomUtil.getRandom;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.comment.CommentState;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    List<Comment> commentList = new ArrayList<>();
    long POST_ID = 1L;
    long USER_ID = 1L;
    long PARENT_COMMENT_ID = 2L;


    @BeforeAll
    public void setUpDummyComment() {
        int repeatCount = 10;

        for (long i = 0; i < repeatCount; i++) {
            Comment entity = Comment.builder()
                .postId(getRandom(repeatCount))
                .userId(getRandom(repeatCount))
                .text("comment " + i)
                .status(CommentState.OPEN)
                .build();

            if (i == 0) {
                entity = entity.toBuilder().postId(POST_ID).build();
            }

            if (i % 2 == 0) {
                entity = entity.toBuilder().userId(USER_ID).pId(PARENT_COMMENT_ID).build();
            }

            entity = commentRepository.saveAndFlush(entity);
            commentList.add(entity);
        }
    }

    @AfterAll
    public void removeDummyComment() {
        commentRepository.deleteAll(commentList);
    }

    @Test
    @DisplayName("댓글 입력 테스트")
    public void commentInsertTest() {
        int repeatCount = 10;

        for (int i = 0; i < repeatCount; i++) {
            Comment entity = Comment.builder()
                .postId(getRandom(repeatCount))
                .userId(getRandom(repeatCount))
                .text("comment " + i)
                .status(CommentState.OPEN)
                .build();

            commentList.add(commentRepository.saveAndFlush(entity));
        }
    }

    @Test
    @DisplayName("댓글 업데이트 테스트")
    public void commentUpdateTest() {
        int i = 1;

        Comment com = commentRepository.findById((long) 1).orElseThrow();
        com.setText("Updated Text");
        com = commentRepository.saveAndFlush(com);

        log.info(com.toString());
    }

    @Test
    @DisplayName("댓글 전체 조회")
    public void commentSelectAllTest() {
        List<Comment> all = commentRepository.findAll();

        all.stream()
            .forEach(comment -> log.info(comment.toString() + "\n"));
    }

    @Test
    @DisplayName("CommentRepository(QueryDsl) 테스트\nfindAllByPostId()")
    void findAllByPostIdTest() {
        List<Comment> list = commentRepository.findAllByPostId(POST_ID);

        list.forEach(comment -> log.info(comment.toString()));
    }

    @Test
    @DisplayName("CommentRepository(JPA Repository) 테스트\nfindById()")
    void findByCommentIdTest() {
        Comment comment = commentRepository.findById(1L).orElse(null);

        log.info(comment.toString());
    }

    @Test
    @DisplayName("CommentRepository(QueryDSL) 테스트\nfindAllByParentId()")
    void findAllByParentId() {
        List<Comment> list = commentRepository.findAllByParentId(PARENT_COMMENT_ID);

        list.forEach(comment -> log.info(comment.toString()));
    }

    @Test
    @DisplayName("CommentRepository(QueryDSL) 테스트\nfindAllByUserId()")
    void findAllByUserId() {
        List<Comment> list = commentRepository.findAllByUserId(USER_ID);

        list.forEach(comment -> log.info(comment.toString()));
    }
}
