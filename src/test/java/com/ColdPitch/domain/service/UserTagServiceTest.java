package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Tag;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.UserTag;
import com.ColdPitch.domain.entity.dto.tag.TagRequestDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.dto.usertag.UserTagRequestDto;
import com.ColdPitch.domain.repository.UserTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class UserTagServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserTagService userTagService;
    @Autowired
    private UserTagRepository userTagRepository;

    private User user;
    private Tag tag1, tag2;

    @Test
    @DisplayName("유저회원 테그 입력 기능 확인")
    public void addUserTag() {
        //given

        // when
        userTagService.setTag(new UserTagRequestDto(List.of("tag1", "tag2")), user);

        //then
        List<UserTag> allUserTags = userTagRepository.findAll();
        assertThat(allUserTags.size()).isEqualTo(2);
        assertThat(allUserTags.get(0).getTag()).isEqualTo(tag1);
        assertThat(allUserTags.get(0).getUser()).isEqualTo(user);
        assertThat(allUserTags.get(1).getTag()).isEqualTo(tag2);
        assertThat(allUserTags.get(1).getUser()).isEqualTo(user);
    }

    @BeforeEach
    private void initUser() {
        UserRequestDto userRequestDto = new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", "USER");
        userService.signUpUser(userRequestDto);
        user = userService.findUserByEmail(userRequestDto.getEmail());
        tag1 = tagService.createTag(new TagRequestDto("tag1", "tag1"));
        tag2 = tagService.createTag(new TagRequestDto("tag2", "tag2"));
    }

}