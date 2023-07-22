package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.core.manager.FileManager;
import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.domain.entity.dto.user.LoginDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.dto.user.UserResponseDto;
import com.ColdPitch.domain.entity.user.UserType;
import com.ColdPitch.domain.repository.*;
import com.ColdPitch.domain.service.CommentService;
import com.ColdPitch.domain.service.CompanyRegistrationService;
import com.ColdPitch.domain.service.PostService;
import com.ColdPitch.domain.service.UserService;
import com.ColdPitch.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@Slf4j
@AutoConfigureMockMvc
public class UserFileManagerTest {

    @MockBean(name = "userFileManager")
    private FileManager userFileManager;
    @InjectMocks
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private CompanyRegistrationService companyRegistrationService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private DislikeRepository dislikeRepository;
    @Autowired
    private PostRepository postRepository;

    MockMultipartFile avatar = new MockMultipartFile("file", "image.png", "image/png", "<<jpg data>>".getBytes(StandardCharsets.UTF_8));

    @BeforeEach
    public void beforeEach() {
        userService = new UserService(authenticationManagerBuilder, userRepository, passwordEncoder, tokenProvider,
                refreshTokenRepository, companyRegistrationService, commentService, commentRepository,
                postService, likeRepository, dislikeRepository, postRepository, userFileManager);

        when(userFileManager.upload(any(), any())).thenReturn("/test");
    }

    @Test
    @Disabled
    @DisplayName("유저 이미지 업로드 테스트 // 테스트 중")
    public void avatarTest() throws Exception {

        //given
        UserRequestDto dto = new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-1234-1234", UserType.USER);
        UserResponseDto user = userService.signUpUser(dto);
        TokenDto login = userService.login(new LoginDto(dto.getEmail(), dto.getPassword()));
        log.info("login : {}", login.toString());

        //when 유저 이미지 업로드
        mockMvc.perform(multipart("/api/v1/user/{nickname}/avatar", user.getNickname())
                        .file(avatar)
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("/test"))
                .andDo(print());

        //then
        Assertions.assertThat(userRepository.findByEmail(user.getEmail()).get().getAvatar()).isEqualTo("/test");
    }

}
