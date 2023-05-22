package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Dislike;
import com.ColdPitch.domain.entity.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DislikeRepository extends JpaRepository<Dislike,Long> {

    Optional<Dislike> findByUserIdAndPostId(Long id, Long postId);
}
