package com.ColdPitch.domain.entity.comment;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.repository.CommentRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("CommentRepository(QueryDsl) 테스트\nfindAllByPostId()")
    void findAllByPostId() {
        Long postId = 1L;

        List<Comment> commentList = commentRepository.findAllByPostId(postId);

        commentList.forEach(comment -> log.info(comment.toString()));
    }

    @Test
    @DisplayName("CommentRepository(JPA Repository) 테스트\nfindById()")
    void findByCommentI() {
        Long commentId = 1L;

        Comment comment = commentRepository.findById(commentId).orElse(null);

        log.info(comment.toString());
    }
}
