package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.dto.post.PostRequestDto;
import com.ColdPitch.domain.entity.dto.post.PostResponseDto;
import com.ColdPitch.domain.service.PostService;
import com.ColdPitch.jwt.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostApiController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "게시글 등록")
    public ResponseEntity<PostResponseDto> createPost(@ApiIgnore Authentication authentication, @RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.status(200).body(postService.createPost(authentication.getName(), postRequestDto));
    }

    @PatchMapping
    @Operation(summary = "게시글 수정")
    public ResponseEntity<PostResponseDto> updatePost(@ApiIgnore Authentication authentication, @RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.status(200).body(postService.updatePost(authentication.getName(), postRequestDto));
    }

    @PatchMapping("/delete")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<String> deletePost(@ApiIgnore Authentication authentication, @RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.status(200).body(postService.deletePost(authentication.getName(), postRequestDto));
    }

    @GetMapping
    @Operation(summary = "게시글 조회")
    public ResponseEntity<PostResponseDto> getPost(@ApiIgnore Authentication authentication, @RequestParam Long postId) {
        return ResponseEntity.status(200).body(postService.getPost(authentication.getName(), postId));
    }


}
