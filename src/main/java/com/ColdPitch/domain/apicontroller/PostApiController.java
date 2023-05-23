package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.dto.post.PostRequestDto;
import com.ColdPitch.domain.entity.dto.post.PostResponseDto;
import com.ColdPitch.domain.entity.post.PostState;
import com.ColdPitch.domain.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
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
    public ResponseEntity<PostResponseDto> createPost(@ApiIgnore Authentication authentication,
        @RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.status(200)
            .body(postService.createPost(authentication.getName(), postRequestDto));
    }

    @PatchMapping
    @Operation(summary = "게시글 수정")
    public ResponseEntity<PostResponseDto> updatePost(@ApiIgnore Authentication authentication,
        @RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.status(200)
            .body(postService.updatePost(authentication.getName(), postRequestDto));
    }

    @PatchMapping("/{postId}/{status}")
    @Operation(summary = "게시글 상태변경")
    public ResponseEntity<PostResponseDto> changeStatus(@ApiIgnore Authentication authentication,
        @PathVariable Long postId, @PathVariable PostState status) {
        return ResponseEntity.status(200)
            .body(postService.postStateChange(authentication.getName(), postId, status));
    }

    @GetMapping("{postId}")
    @Operation(summary = "게시글 조회")
    public ResponseEntity<PostResponseDto> getPost(@ApiIgnore Authentication authentication,
        @PathVariable Long postId) {
        return ResponseEntity.status(200)
            .body(postService.getPost(authentication.getName(), postId));
    }

    @PostMapping("/{postId}/like")
    @Operation(summary = "게시글 좋아요")
    public ResponseEntity<PostResponseDto> likePost(@ApiIgnore Authentication authentication,
        @PathVariable Long postId) {
        return ResponseEntity.status(200)
            .body(postService.likePost(authentication.getName(), postId));
    }

    @PostMapping("/{postId}/dislike")
    @Operation(summary = "게시글 싫어요")
    public ResponseEntity<PostResponseDto> dislikePost(@ApiIgnore Authentication authentication,
        @PathVariable Long postId) {
        return ResponseEntity.status(200)
            .body(postService.dislikePost(authentication.getName(), postId));
    }

    @GetMapping("/all")
    @Operation(summary = "전체 게시글 조회")
    public ResponseEntity<List<PostResponseDto>> findAll() {
        return ResponseEntity.status(200)
            .body(postService.findAllPosts());
    }

}


