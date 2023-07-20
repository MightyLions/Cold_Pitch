package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.core.manager.AWSFileManager;
import com.ColdPitch.core.manager.UserFileManager;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.domain.entity.dto.user.LoginDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.dto.user.UserResponseDto;
import com.ColdPitch.domain.entity.user.UserType;
import com.ColdPitch.domain.repository.UserRepository;
import com.ColdPitch.domain.service.RefreshTokenService;
import com.ColdPitch.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class UserAuthApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("유저 회원가입 확인")
    public void signUpUserControllerTest() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        UserRequestDto userRequestDto = new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", UserType.USER);
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        //when
        mockMvc.perform(post("/api/v1/auth/user/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("nickname").value("nickname"))
                .andExpect(jsonPath("email").value("email@naver.com"));
        //then
        User find = userRepository.findByEmail("email@naver.com").orElseThrow();
        assertTrue(passwordEncoder.matches("password", find.getPassword()));
        assertThat(find.getNickname()).isEqualTo("nickname");

    }

    @Test
    @DisplayName("유저 로그인 성공 확인")
    public void loginSuccessTest() throws Exception {
        //given 유저 저장
        UserRequestDto userRequestDto = new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", UserType.USER);
        userService.signUpUser(userRequestDto);


        //when
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = new LoginDto("email@naver.com", "password");
        String requestBody = objectMapper.writeValueAsString(loginDto);

        //when
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("유저 로그인 실패 확인")
    public void loginFailTest() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = new LoginDto("eamil@naver.com", "password");
        String requestBody = objectMapper.writeValueAsString(loginDto);

        //when 회원가입 되지 않은 유저 로그인시에
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("유저 로그아웃 테스트")
    public void logoutTest() throws Exception {
        //given
        UserResponseDto user = userService.signUpUser(new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", UserType.USER));
        Assertions.assertThat(userService.findUserByEmail(user.getEmail()).getNickname()).isEqualTo(user.getNickname());

        LoginDto loginDto = new LoginDto("email@naver.com", "password");
        TokenDto login = userService.login(loginDto);

        //when 회원가입 되지 않은 유저 로그인시에
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + login.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Assertions.assertThat(refreshTokenService.findKey("email@naver.com")).isEqualTo(Optional.empty());
    }


    @Test
    @Disabled
    @DisplayName("유저 이미지 업로드 테스트 // 테스트 중")
    public void avatarTest() throws Exception {
        //mocking
        AWSFileManager mockObject = mock(AWSFileManager.class);
        UserFileManager mock2Object = mock(UserFileManager.class);
        MockMultipartFile avatar = new MockMultipartFile("file", "image.png", "image/png", "<<jpg data>>".getBytes(StandardCharsets.UTF_8));
        when(mockObject.upload("test", avatar)).thenReturn("/test");
        when(mock2Object.upload("test", avatar)).thenReturn("/test");

        //given
        UserResponseDto user = userService.signUpUser(new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", UserType.USER));
        Assertions.assertThat(userService.findUserByEmail(user.getEmail()).getNickname()).isEqualTo(user.getNickname());

        LoginDto loginDto = new LoginDto("email@naver.com", "password");
        TokenDto login = userService.login(loginDto);

        Assertions.assertThat(mockObject.upload("test", avatar)).isEqualTo("/test");

        //when 유저 이미지 업로드
        mockMvc.perform(multipart("/api/v1/user/{nickname}/avatar", user.getNickname())
                        .file(avatar)
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("string").value("/test"))
                .andDo(print());

        //then
        Assertions.assertThat(userRepository.findByEmail(user.getEmail()).get().getAvatar()).isEqualTo("/test");
        verify(mockObject, times(3));
    }


}