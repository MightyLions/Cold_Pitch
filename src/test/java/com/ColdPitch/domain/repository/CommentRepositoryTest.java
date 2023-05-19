package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    List<Comment> commentList = new ArrayList<>();

    @BeforeAll
    public void setUpDummyComment() {
        int repeatCount = 10;

        for (int i = 0; i < repeatCount; i++) {
            Comment entity = Comment.builder()
                .postId(getRandom(repeatCount))
                .userId(getRandom(repeatCount))
                .text("comment " + i)
                .build();

            entity = commentRepository.saveAndFlush(entity);
            commentList.add(entity);
        }
    }

    @AfterAll
    public void removeDummyComment() {
        commentRepository.deleteAll(commentList);
    }

    @Test
    @Order(1)
    @DisplayName("댓글 입력 테스트")
    public void commentInsertTest() {
        int repeatCount = 10;

        for (int i = 0; i < repeatCount; i++) {
            Comment entity = Comment.builder()
                .postId(getRandom(repeatCount))
                .userId(getRandom(repeatCount))
                .text("comment " + i)
                .build();

            commentList.add(commentRepository.saveAndFlush(entity));
        }
    }

    @Test
    @Order(2)
    @DisplayName("댓글 업데이트 테스트")
    public void commentUpdateTest() {
        int i = 1;

        Comment com = commentRepository.findById((long) 1).orElseThrow();
        com.setText("Updated Text");
        com = commentRepository.saveAndFlush(com);

        log.info(com.toString());
    }

    @Test
    @Order(3)
    @DisplayName("댓글 전체 조회")
    public void commentSelectAllTest() {
        List<Comment> all = commentRepository.findAll();

        all.stream()
            .forEach(comment -> log.info(comment.toString() + "\n"));
    }

    @Test
    @DisplayName("CommentRepository(QueryDsl) 테스트\nfindAllByPostId()")
    void findAllByPostIdTest() {
        Long postId = 1L;

        List<Comment> list = commentRepository.findAllByPostId(postId);

        list.forEach(comment -> log.info(comment.toString()));
    }

    @Test
    @DisplayName("CommentRepository(JPA Repository) 테스트\nfindById()")
    void findByCommentIdTest() {
        Long commentId = 1L;

        Comment comment = commentRepository.findById(commentId).orElse(null);

        log.info(comment.toString());
    }

    /**
     * 랜덤 Long을 생성하는 메소드
     *
     * @param end inclusive
     * @return 0부터 end 이하까지의 범위
     */
    private long getRandom(int end) {
        Random random = new Random();

        return (long) random.nextInt(end);
    }
}
