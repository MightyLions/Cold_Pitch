package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Post;
import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {
}
