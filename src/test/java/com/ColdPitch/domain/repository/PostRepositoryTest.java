package com.ColdPitch.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.entity.post.PostState;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
@Transactional
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    public void 게시글_초기값_생성() {
        Post post = Post.builder()
            .title("firstTitle")
            .text("firstText")
            .status("OPEN")
            .category("firstCategory")
            .userId((long) 1)
            .build();
        postRepository.save(post);
    }

    @AfterEach
    public void 게시글_초기값_삭제() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    public void 게시글_생성_테스트() {
        Post post = Post.builder()
            .title("testTitle")
            .text("testText")
            .status("OPEN")
            .category("testCategory")
            .userId((long) 2)
            .build();

        postRepository.save(post);

        Post post2 = postRepository.findById(post.getId()).orElseThrow();
        assertThat(post.equals(post2)).isEqualTo(true);
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void 게시글_수정_테스트() {
        Post post = postRepository.findAll().get(0);
        post.setTitle("updatedTitle");
        post.setText("updatedText");
        post.setCategory("updatedCategory");
        post.setStatus("UpdatedStatus");
        postRepository.save(post);

        Post post2 = postRepository.findById(post.getId()).orElseThrow();
        assertThat(post2.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    public void 게시글_조회_테스트() {
        List<Post> postList = postRepository.findAll();
        postList.forEach(p -> log.info(p.toString()));
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void 게시글_삭제_테스트() {
        Post post = postRepository.findAll().get(0);
        postRepository.deleteById(post.getId());
        assertThat(postRepository.findById((long) 1).isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("게시글 상태변환 테스트")
    public void 게시글_닫기_테스트() {
        Post post = postRepository.findAll().get(0);
        post.setStatus(PostState.CLOSED.getStatus());
        postRepository.save(post);

        Post post2 = postRepository.findById(post.getId()).orElseThrow();
        assertThat(post.getStatus()).isEqualTo(post2.getStatus()).isEqualTo(PostState.CLOSED.getStatus());
    }

}
