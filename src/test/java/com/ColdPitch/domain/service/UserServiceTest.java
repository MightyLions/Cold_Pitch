package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Slf4j
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유저회원 로그인을 확인한다. ")
    void signup() {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", "USER");

        // when
        User savedUser = userService.signup(userRequestDto);

        //then
        assertThat(savedUser.getEmail()).isEqualTo("email@naver.com");
        assertThat(savedUser.getPhoneNumber()).isEqualTo("010-7558-2452");
        assertThat(savedUser.getUserType().name()).isEqualTo("USER");
        assertThat(savedUser.getNickname()).isEqualTo("nickname");
        assertThat(savedUser.getName()).isEqualTo("name");
        assertTrue(checkPassword(savedUser.getPassword(), "password"));
    }


    public boolean checkPassword(String password, String exPassword) {
        return passwordEncoder.matches(exPassword, password);
    }
}