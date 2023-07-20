package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Tag;
import com.ColdPitch.domain.entity.dto.tag.TagRequestDto;
import com.ColdPitch.domain.entity.dto.usertag.TagResponseDto;
import com.ColdPitch.domain.repository.TagRepository;
import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;

    @Transactional
    public TagResponseDto createTag(TagRequestDto tagRequestDto) {
        //태그 이름 중복확인
        String trimmedTagName = tagRequestDto.getTagName().trim();
        if (findTagByTagName(trimmedTagName).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_TAG_NAME);
        }

        return TagResponseDto.of(tagRepository.save(tagRequestDto.toEntity()));
    }

    public Optional<TagResponseDto> findTagByTagName(String name) {
        return TagResponseDto.of(tagRepository.findByTagName(name));
    }

    public Tag findTagByTagNameOrThrowException(String name) {
        return tagRepository
                .findByTagName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TAG_NAME));
    }


    public List<TagResponseDto> getAllTags() {
        return tagRepository.findAll().stream().map(TagResponseDto::of).collect(Collectors.toList());
    }

}
