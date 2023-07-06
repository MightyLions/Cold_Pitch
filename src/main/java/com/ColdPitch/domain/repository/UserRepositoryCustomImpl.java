package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.user.UserResponseDto;
import com.ColdPitch.domain.repository.support.Querydsl4RepositorySupport;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserRepositoryCustomImpl extends Querydsl4RepositorySupport implements UserRepositoryCustom {
    public UserRepositoryCustomImpl() {
        super(User.class);
    }

}
