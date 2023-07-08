package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    List<Comment> findAllByUserId(Long userId);
    List<Comment> findAllByPostId(Long postId);
}
