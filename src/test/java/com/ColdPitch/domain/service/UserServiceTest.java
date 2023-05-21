package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.domain.entity.dto.user.LoginDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.dto.user.UserResponseDto;
import com.ColdPitch.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
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
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("유저회원 회원가입을 확인한다. ")
    public void signup() {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", "USER");

        // when
        userService.signup(userRequestDto);

        //then
        User savedUser = userService.findUserByEmail(userRequestDto.getEmail());
        assertThat(savedUser.getEmail()).isEqualTo("email@naver.com");
        assertThat(savedUser.getPhoneNumber()).isEqualTo("010-7558-2452");
        assertThat(savedUser.getUserType().name()).isEqualTo("USER");
        assertThat(savedUser.getNickname()).isEqualTo("nickname");
        assertThat(savedUser.getName()).isEqualTo("name");
        assertTrue(checkPassword(savedUser.getPassword(), "password"));
    }

    @Test
    @DisplayName("유저 회원 로그인을 성공을 확인한다.")
    public void loginTestsuite() {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", "USER");
        userService.signup(userRequestDto);

        //when
        LoginDto loginDto = new LoginDto("email@naver.com", "password");
        TokenDto login = userService.login(loginDto);

        //then ( 생성된 토큰의 유효성을 확인한다. )
        assertThat(tokenProvider.getAuthentication(login.getAccessToken()).getName()).isEqualTo("email@naver.com");
    }

    @Test
    @DisplayName("유저 회원 로그인을 실패를 확인한다.")
    public void loginFailTestsuite() {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", "USER");
        userService.signup(userRequestDto);

        //when
        LoginDto loginDto = new LoginDto("email@naver.com", "pass");
        Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.login(loginDto);
        });

    }

    @Test
    @DisplayName("유저 로그아웃을 확인한다.")
    public void logoutTestsuite() {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", "USER");
        userService.signup(userRequestDto);
        LoginDto loginDto = new LoginDto("email@naver.com", "password");
        TokenDto login = userService.login(loginDto);

        //when
        userService.logout(userRequestDto.getEmail());

        //then ( 생성된 토큰의 유효성을 확인한다. )
        assertThat(refreshTokenService.findKey(userRequestDto.getEmail())).isEqualTo(java.util.Optional.empty());
    }

    private boolean checkPassword(String password, String exPassword) {
        return passwordEncoder.matches(exPassword, password);
    }
}