package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
}
