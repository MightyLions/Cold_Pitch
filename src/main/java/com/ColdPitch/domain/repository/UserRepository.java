package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);

    Optional<User> findOneWithAuthoritiesByEmail(String email);

    Optional<User> findByNickname(String nickname);

    void deleteByEmail(String email);

    @Query(value = "SELECT * FROM user", nativeQuery = true)
    Optional<List<User>> findAllUserIncludeDeletedUser(); //삭제된 유저까지 전부 조회

    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    Optional<List<User>> findUserByEmailIncludeDeletedUser(@Param("email") String email); //삭제된 유저 포함해서 email중에 조회
}
