package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {
    List<Post> findByIdIn(Set<Long> postIds);
}
