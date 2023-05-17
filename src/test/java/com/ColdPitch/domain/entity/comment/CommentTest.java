package com.ColdPitch.domain.entity.comment;

import com.ColdPitch.aop.LoggingAspect;
import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.repository.CommentRepository;
import java.util.Random;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentTest {
    @Autowired
    CommentRepository comRepo;

    @BeforeTestExecution
    public void setUpDummyComment() {
        int repeatCount = 10;

        for (int i = 0; i < repeatCount; i++) {
            Comment entity = Comment.builder()
                .posterId(getRandom(repeatCount))
                .userId(getRandom(repeatCount))
                .text("comment " + i)
                .build();

            comRepo.save(entity);
        }
    }

    @Test
    @Order(1)
    @DisplayName("댓글 입력 테스트")
    public void commentInsertTest() {
        int repeatCount = 10;

        for (int i = 0; i < repeatCount; i++) {
            Comment entity = Comment.builder()
                .posterId(getRandom(repeatCount))
                .userId(getRandom(repeatCount))
                .text("comment " + i)
                .build();

            comRepo.save(entity);
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
        comRepo.save(com);
    }

    /**
     * 랜덤 Long을 생성하는 메소드
     * @param end inclusive
     * @return 0부터 end 이하까지의 범위
     */
    public long getRandom(int end) {
        Random random = new Random();

        return (long) random.nextInt(end);
    }

    public long getRandom() {
        Random random = new Random();

        return random.nextLong();
    }
}
