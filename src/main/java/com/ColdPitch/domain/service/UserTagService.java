package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Tag;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.UserTag;
import com.ColdPitch.domain.entity.dto.user.CompanyResponseDto;
import com.ColdPitch.domain.entity.dto.usertag.TagResponseDto;
import com.ColdPitch.domain.entity.dto.usertag.TagRequestDto;
import com.ColdPitch.domain.repository.UserRepository;
import com.ColdPitch.domain.repository.UserTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    private final UserRepository userRepository;

    @Transactional
    public List<TagResponseDto> setTag(TagRequestDto TagRequestDto, User user) {
        initUserTag(user); //TODO 해당 부분은 추후에 리팩토링 (수정 하는 경우 같은 메서드를 사용할 것 인지)
        List<TagResponseDto> userTagResponse = new ArrayList<>();
        for (String nowTag : TagRequestDto.getUserTag()) {
            Tag tag = tagService.findTagByTagNameOrThrowException(nowTag);
            userTagResponse.add(TagResponseDto.of(userTagRepository.save(new UserTag(user, tag))));
        }
        //유저에게 테그 달기
        return userTagResponse;
    }

    @Transactional
    public void initUserTag(User user) {
        for (int i=user.getUserTags().size()-1;0<=i;i--) {
            UserTag userTag = user.getUserTags().get(i);
            userTag.delete();
            userTagRepository.delete(userTag);
        }
    }


    //태그를 만족하는 모든 회사(or)
    public List<CompanyResponseDto> findCompanyByEachAllTags(TagRequestDto TagRequestDto) {
        List<Tag> findTags = getTags(TagRequestDto);

        return userRepository.findCompanyByAllEachTags(findTags).stream().map(CompanyResponseDto::new).collect(Collectors.toList());
    }

    //태그를 만족하는 모든 회사(and)
//    public List<CompanyResponseDto> findCompanyByAndTags(TagRequestDto TagRequestDto) {
//        List<Tag> findTags = getTags(TagRequestDto);
//
//        return userRepository.findCompanyByAndTags(findTags).stream().map(CompanyResponseDto::new).collect(Collectors.toList());
//    }

    @NotNull
    private List<Tag> getTags(TagRequestDto TagRequestDto) {
        List<Tag> findTags = new ArrayList<>();
        for (String nowTag : TagRequestDto.getUserTag()) {
            Tag tag = tagService.findTagByTagNameOrThrowException(nowTag);
            findTags.add(tag);
        }
        return findTags;
    }

    public List<TagResponseDto> findMyTag(User user) {
        return user.getUserTags().stream().map(TagResponseDto::of).collect(Collectors.toList());
    }
}
