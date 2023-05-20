package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Dislike;
import com.ColdPitch.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DislikeRepository extends JpaRepository<Dislike,Long> {

}
