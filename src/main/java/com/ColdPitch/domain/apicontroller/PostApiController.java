package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.dto.post.PostRequestDto;
import com.ColdPitch.domain.entity.dto.post.PostResponseDto;
import com.ColdPitch.domain.entity.post.PostState;
import com.ColdPitch.domain.service.PostService;
import com.ColdPitch.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostApiController {

    private final PostService postService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "게시글 등록")
    public ResponseEntity<PostResponseDto> createPost(
        @Valid @RequestPart PostRequestDto postRequestDto,
        @RequestPart List<MultipartFile> files) {
        return ResponseEntity.status(200)
            .body(postService.createPost(postRequestDto,files));
    }

    @PatchMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "게시글 수정")
    public ResponseEntity<PostResponseDto> updatePost(
        @Valid @RequestPart PostRequestDto postRequestDto,
        @RequestPart List<MultipartFile> files) {
        return ResponseEntity.status(200)
            .body(postService.updatePost(postRequestDto,files));
    }

    @PatchMapping("/{postId}/status")
    @Operation(summary = "게시글 상태변경")
    public ResponseEntity<PostResponseDto> changeStatus(
        @PathVariable Long postId,
        @Valid @RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.status(200)
            .body(postService.postStateChange(postRequestDto));
    }

    @GetMapping("{postId}")
    @Operation(summary = "게시글 조회")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.status(200)
            .body(postService.findPost(postId));
    }

    @PostMapping("/{postId}/like")
    @Operation(summary = "게시글 좋아요")
    public ResponseEntity<PostResponseDto> likePost(@PathVariable Long postId) {
        return ResponseEntity.status(200)
            .body(postService.likePost(postId));
    }

    @PostMapping("/{postId}/dislike")
    @Operation(summary = "게시글 싫어요")
    public ResponseEntity<PostResponseDto> dislikePost(@PathVariable Long postId) {
        return ResponseEntity.status(200)
            .body(postService.dislikePost(postId));
    }

    @GetMapping("/all")
    @Operation(summary = "전체 게시글 조회")
    public ResponseEntity<List<PostResponseDto>> findAll() {
        return ResponseEntity.status(200)
            .body(postService.findAllPosts());
    }

}


