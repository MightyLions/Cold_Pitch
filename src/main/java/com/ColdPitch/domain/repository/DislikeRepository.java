package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Dislike;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DislikeRepository extends JpaRepository<Dislike,Long> {

    Optional<Dislike> findByUserIdAndPostId(Long id, Long postId);

    Optional<List<Dislike>> findByUserId(Long userId);

    List<Dislike> findByUserIdAndPostIdIn(Long userId, Set<Long> postIds);
}
