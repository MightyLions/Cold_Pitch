package com.ColdPitch.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.entity.post.PostState;
import com.ColdPitch.domain.repository.PostRepository;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @BeforeTestExecution
    public Post 게시글_초기값_생성() {
        Long testId = getRandom(10);
        Post post = Post.builder()
            .title("testTitle")
            .text("testText")
            .status("OPEN")
            .category("testCategory")
            .userId(testId)
            .build();
        postRepository.save(post);
        return post;
    }

    @Test
    @Order(1)
    @DisplayName("게시글 생성 테스트")
    public void 게시글_생성_테스트() {
        Long testId = getRandom(10);
        Post post = Post.builder()
            .title("testTitle")
            .text("testText")
            .status("OPEN")
            .category("testCategory")
            .userId(testId)
            .build();

        postRepository.save(post);

        Post post2 = postRepository.findById(post.getId()).orElseThrow();
        assertThat(post.equals(post2)).isEqualTo(true);
    }

    @Test
    @Order(2)
    @DisplayName("게시글 수정 테스트")
    public void 게시글_수정_테스트() {
        Post post = postRepository.findById((long) 1).orElseThrow();
        post.setTitle("updatedTitle");
        post.setText("updatedText");
        post.setCategory("updatedCategory");
        post.setStatus("UpdatedStatus");
        postRepository.save(post);

        Post post2 = postRepository.findById(post.getId()).orElseThrow();
        assertThat(post2.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    @Order(3)
    @DisplayName("게시글 조회 테스트")
    public void 게시글_조회_테스트() {
        List<Post> postList = postRepository.findAll();
        postList.forEach(p -> log.info(p.toString()));
    }

    @Test
    @Order(4)
    @DisplayName("게시글 삭제 테스트")
    public void 게시글_삭제_테스트() {
        Long testId = (long) 1;
        postRepository.deleteById(testId);
        assertThat(postRepository.findById(testId).isEmpty()).isEqualTo(true);
    }

    @Test
    @Order(5)
    @DisplayName("게시글 상태변환 테스트")
    public void 게시글_닫기_테스트() {
        Post post = 게시글_초기값_생성();
        post.setStatus(PostState.CLOSED.getStatus());
        postRepository.save(post);

        Post post2 = postRepository.findById(post.getId()).orElseThrow();
        assertThat(post.getStatus()).isEqualTo(post2.getStatus()).isEqualTo(PostState.CLOSED.getStatus());
    }

    private long getRandom(int end) {
        Random random = new Random();
        return random.nextInt(end);
    }
}
