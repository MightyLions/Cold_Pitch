package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.Tag;
import com.ColdPitch.domain.entity.dto.tag.TagRequestDto;
import com.ColdPitch.domain.entity.dto.usertag.TagResponseDto;
import com.ColdPitch.domain.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tags")

public class TagApiController {
    private final TagService tagService;

    @Autowired
    public TagApiController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags().stream().map(TagResponseDto::of).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<TagResponseDto> createTag(TagRequestDto tagRequestDto) {
        return ResponseEntity.ok(TagResponseDto.of(tagService.createTag(tagRequestDto)));
    }
}
