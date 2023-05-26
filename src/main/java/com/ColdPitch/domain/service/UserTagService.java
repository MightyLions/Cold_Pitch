package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Tag;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.UserTag;
import com.ColdPitch.domain.entity.dto.usertag.TagResponseDto;
import com.ColdPitch.domain.entity.dto.usertag.UserTagRequestDto;
import com.ColdPitch.domain.repository.UserTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserTagService {
    private final UserTagRepository userTagRepository;
    private final TagService tagService;

    @Transactional
    public List<TagResponseDto> setTag(UserTagRequestDto userTagRequestDto, User user) {
        initUserTag(user); //TODO 해당 부분은 추후에 리팩토링 (수정 하는 경우 같은 메서드를 사용할 것 인지)
        List<TagResponseDto> userTagResponse = new ArrayList<>();
        for (String nowTag : userTagRequestDto.getUserTag()) {
            Tag tag = tagService.findTagByTagNameOrThrowException(nowTag);
            userTagResponse.add(TagResponseDto.of(userTagRepository.save(new UserTag(user, tag))));
        }

        //유저에게 테그 달기
        return userTagResponse;
    }

    private static void initUserTag(User user) {
        for (UserTag userTag : user.getUserTags()) {
            userTag.delete();
        }
    }

    public List<TagResponseDto> findMyTag(User user) {
        return user.getUserTags().stream().map(TagResponseDto::of).collect(Collectors.toList());
    }
}
