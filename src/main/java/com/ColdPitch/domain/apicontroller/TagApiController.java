package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.dto.tag.TagRequestDto;
import com.ColdPitch.domain.entity.dto.usertag.TagResponseDto;
import com.ColdPitch.domain.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Slf4j
public class TagApiController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponseDto> createTag(TagRequestDto tagRequestDto) {
        return ResponseEntity.ok(TagResponseDto.of(tagService.createTag(tagRequestDto)));
    }

    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags().stream().map(TagResponseDto::of).collect(Collectors.toList()));
    }
}
