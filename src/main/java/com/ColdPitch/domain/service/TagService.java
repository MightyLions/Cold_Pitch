package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Tag;
import com.ColdPitch.domain.entity.dto.tag.TagRequestDto;
import com.ColdPitch.domain.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag createTag(TagRequestDto tagRequestDto) {
        return tagRepository.save(Tag.builder().tagName(tagRequestDto.getTagName()).description(tagRequestDto.getDescription()).build());
    }

    public Tag findTagByTagNameOrThrowException(String name) {
        return tagRepository.findByTagName(name).orElseThrow(IllegalAccessError::new);
    }

    public Optional<Tag> findTagByTagName(String name) {
        return tagRepository.findByTagName(name);
    }

}
