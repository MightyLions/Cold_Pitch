package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.domain.entity.dto.user.LoginDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.user.UserType;
import com.ColdPitch.domain.repository.UserRepository;
import com.ColdPitch.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private User user1;
    private TokenDto userToken1;

    @Test
    @DisplayName("유저 탈퇴 확인")
    public void loginFailTest() throws Exception {
        //given

        //when 회원가입 되지 않은 유저 로그인시에
        mockMvc.perform(delete("/api/v1/user")
                        .header("Authorization", "Bearer " + userToken1.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertThat(userRepository.findByEmail(user1.getEmail())).isEmpty(); //user1이 조회되지 않는다.
    }

    @BeforeEach
    public void initUser() {
        userService.signUpUser(new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", UserType.USER));
        userService.signUpUser(new UserRequestDto("Anickname", "Aname", "Apassword", "Aemail@naver.com", "010-7558-2444", UserType.ADMIN));
        user1 = userRepository.findByNickname("nickname").orElseThrow();
        userToken1 = userService.login(new LoginDto("email@naver.com", "password"));
    }


}