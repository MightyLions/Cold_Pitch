package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {
}
