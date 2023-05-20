package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Optional<User> findOneWithAuthoritiesByEmail(String email);
}
