package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Tag;
import com.ColdPitch.domain.entity.dto.tag.TagRequestDto;
import com.ColdPitch.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;

    @Transactional
    public Tag createTag(TagRequestDto tagRequestDto) {
        return tagRepository.save(Tag.builder()
                .tagName(tagRequestDto.getTagName())
                .description(tagRequestDto.getDescription())
                .userTags(new ArrayList<>())
                .build());
    }

    public Optional<Tag> findTagByTagName(String name) {
        return tagRepository.findByTagName(name);
    }

    public Tag findTagByTagNameOrThrowException(String name) {
        return tagRepository.findByTagName(name).orElseThrow(IllegalAccessError::new);
    }


    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

}
