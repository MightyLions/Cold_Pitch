package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {

    Optional<Like> findByUserIdAndPostId(Long id, Long postId);
}
