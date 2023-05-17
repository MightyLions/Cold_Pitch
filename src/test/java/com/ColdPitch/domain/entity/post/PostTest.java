package com.ColdPitch.domain.entity.post;

import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.repository.PostRepository;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostTest {

    @Autowired
    PostRepository postRepository;

    @Test
    @Order(1)
    @DisplayName("게시글 작성 테스트")
    public void postCreateTest() {
        int repeatCount = 10;
        for (int i = 0; i < repeatCount; i++) {
            Post post = Post.builder()
                .title("testTitle")
                .text("testText")
                .status("OPEN")
                .category("testCategory")
                .userId(getRandom(repeatCount))
                .build();
            postRepository.save(post);
        }
    }

    @Test
    @Order(2)
    @DisplayName("게시글 수정 테스트")
    public void postUpdateTest() {
        Post post = postRepository.findById(getRandom(10)).orElseThrow();
        post.setTitle("updatedTitle");
        post.setText("updatedText");
        post.setCategory("updatedCategory");
        post.setStatus("UpdatedStatus");
        postRepository.save(post);
    }

    @Test
    @Order(3)
    @DisplayName("게시글 조회 테스트")
    public void postListTest() {
        List<Post> postList = postRepository.findAll();
        postList.forEach(p -> log.info(p.toString()));
    }

    @Test
    @Order(4)
    @DisplayName("게시글 삭제 테스트")
    public void postDeleteTest() {
        postRepository.deleteById(getRandom(10));
    }

    private long getRandom(int end) {
        Random random = new Random();
        return random.nextInt(end);
    }
}
