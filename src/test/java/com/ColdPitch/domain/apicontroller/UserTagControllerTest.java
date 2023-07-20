package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.Tag;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.UserTag;
import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.domain.entity.dto.tag.TagRequestDto;
import com.ColdPitch.domain.entity.dto.user.LoginDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.dto.usertag.SaveTagRequestDto;
import com.ColdPitch.domain.entity.dto.usertag.TagResponseDto;
import com.ColdPitch.domain.entity.user.UserType;
import com.ColdPitch.domain.repository.UserRepository;
import com.ColdPitch.domain.repository.UserTagRepository;
import com.ColdPitch.domain.service.TagService;
import com.ColdPitch.domain.service.UserService;
import com.ColdPitch.domain.service.UserTagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class UserTagControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserTagRepository userTagRepository;
    @Autowired
    private UserTagService userTagService;

    private User user;
    private TokenDto login;
    private TagResponseDto tag1, tag2, tag3;

    @Test
    @DisplayName("유저 테그입력 테스트")
    public void setTagTest() throws Exception {
        //given
        Assertions.assertThat(userService.findUserByEmail(user.getEmail()).getNickname()).isEqualTo(user.getNickname());
        ObjectMapper objectMapper = new ObjectMapper();
        SaveTagRequestDto SaveTagRequestDto = new SaveTagRequestDto(List.of("tag1", "tag2"));
        String requestBody = objectMapper.writeValueAsString(SaveTagRequestDto);

        //when 회원가입 되지 않은 유저 로그인시에
        mockMvc.perform(post("/api/v1/usertag")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .header("Authorization", "Bearer " + login.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("tag1"))
                .andExpect(jsonPath("$[1].name").value("tag2"))
                .andDo(print());

        //then
        List<UserTag> allUserTags = userTagRepository.findAll();
        assertThat(allUserTags.size()).isEqualTo(2);
        Tag expected1 = allUserTags.get(0).getTag();
        assertThat(expected1.getTagName()).isEqualTo(tag1.getName());
        assertThat(allUserTags.get(0).getUser()).isEqualTo(user);
        expected1 = allUserTags.get(1).getTag();
        assertThat(expected1.getTagName()).isEqualTo(tag2.getName());
        assertThat(allUserTags.get(1).getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("유저 테그조회 테스트")
    public void findTagTest() throws Exception {
        //given
        userTagService.setTag(new SaveTagRequestDto(List.of("tag1", "tag2", "tag3")), user);

        //when 회원가입 되지 않은 유저 로그인시에
        mockMvc.perform(get("/api/v1/usertag")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + login.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("tag1"))
                .andExpect(jsonPath("$[0].description").value("tag1"))
                .andExpect(jsonPath("$[1].name").value("tag2"))
                .andExpect(jsonPath("$[1].description").value("tag2"))
                .andExpect(jsonPath("$[2].name").value("tag3"))
                .andExpect(jsonPath("$[2].description").value("tag3"))
                .andExpect(jsonPath("$[3]").doesNotExist())
                .andDo(print());
    }

    @BeforeEach
    public void initUser() {
        userService.signUpUser(new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", UserType.USER));
        user = userRepository.findByNickname("nickname").orElseThrow();
        login = userService.login(new LoginDto("email@naver.com", "password"));
        tag1 = tagService.createTag(new TagRequestDto("tag1", "tag1"));
        tag2 = tagService.createTag(new TagRequestDto("tag2", "tag2"));
        tag3 = tagService.createTag(new TagRequestDto("tag3", "tag3"));
    }
}