package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {
}
