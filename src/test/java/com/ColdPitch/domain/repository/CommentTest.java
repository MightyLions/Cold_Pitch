package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.repository.CommentRepository;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentTest {

    @Autowired
    CommentRepository comRepo;
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

            entity = comRepo.saveAndFlush(entity);
            commentList.add(entity);
        }
    }

    @AfterEach
    public void removeDummyComment() {
        comRepo.deleteAll(commentList);
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

            comRepo.saveAndFlush(entity);
        }

//        for (long i = 0; i < repeatCount; i++) {
//            log.info(comRepo.findById(i).toString());
//        }
    }

    @Test
    @Order(2)
    @DisplayName("댓글 업데이트 테스트")
    public void commentUpdateTest() {
        int i = 1;

        Comment com = comRepo.findById((long) 1).orElseThrow();
        com.setText("Updated Text");
        com = comRepo.saveAndFlush(com);

        log.info(com.toString());
    }

    @Test
    @Order(3)
    @DisplayName("댓글 전체 조회")
    public void commentSelectAllTest() {
        List<Comment> all = comRepo.findAll();

        all.stream()
            .forEach(comment -> log.info(comment.toString() + "\n"));
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
