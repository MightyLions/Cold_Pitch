package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Optional<List<Comment>> findByUserId(Long userId);
}
