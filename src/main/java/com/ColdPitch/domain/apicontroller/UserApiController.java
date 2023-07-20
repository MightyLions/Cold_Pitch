package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.config.security.JwtConfig;
import com.ColdPitch.domain.entity.dto.post.PostResponseDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.dto.user.UserResponseDto;
import com.ColdPitch.domain.repository.UserRepository;
import com.ColdPitch.domain.service.UserService;
import com.ColdPitch.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/user")
public class UserApiController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "유저 전체 조회 ( 이후에 ADMIN 권한으로 열기)")
    public ResponseEntity<List<UserResponseDto>> findAllUser() {
        return ResponseEntity.status(200).body(userService.findAllUser());
    }

    @GetMapping("/{nickname}/posts")
    @Operation(summary = "유저가 작성한 글 전체 조회 ")
    public ResponseEntity<List<PostResponseDto>> findMyPost(@PathVariable String nickname) {
        return ResponseEntity.status(200).body(userService.findMyWritePost(SecurityUtil.getCurrentUserEmail().orElseThrow(IllegalAccessError::new)));
    }


    @GetMapping("/{nickname}")
    @Operation(summary = "특정 유저 조회")
    public ResponseEntity<UserResponseDto> findMyProfile(@PathVariable("nickname") String nickname) {
        return ResponseEntity.status(200).body(userService.findByNickName(nickname));
    }

    @GetMapping("/{nickname}/avatar")
    @Operation(summary = "유저 사진 확인")
    public ResponseEntity<String> findAvatar(@PathVariable("nickname") String nickname) {
        return ResponseEntity.status(200).body(userService.findAvatar(nickname));
    }

    @PatchMapping
    @Operation(summary = "유저 수정", description = "유저 이름, 전화번호, 닉네임, password 변경 기능")
    public ResponseEntity<UserResponseDto> updateProfile(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(200).body(userService.updateProfile(SecurityUtil.getCurrentUserEmail().orElseThrow(IllegalAccessError::new), userRequestDto));
    }

    @PostMapping("/{nickname}/avatar")
    @Operation(summary = "유저 사진 업로드")
    public ResponseEntity<String> uploadAvatar(@PathVariable("nickname") String nickname, @RequestPart(value = "file") MultipartFile multipartFile) {
        return ResponseEntity.status(200).body(userService.uploadAvatar(nickname, multipartFile));
    }

    @DeleteMapping
    @Operation(summary = "유저 탈퇴")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser(SecurityUtil.getCurrentUserEmail().orElseThrow(IllegalAccessError::new));
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/user/evaluated-posts")
    @Operation(summary = "유저가 평가한 글 전체 조회 (좋아요, 싫어요, 댓글)")
    public ResponseEntity<List<PostResponseDto>> getEvaluatedPostsByUser() {
        List<PostResponseDto> posts = userService.getEvaluatedPostsByUserFetch(
                SecurityUtil.getCurrentUserEmail().orElseThrow(IllegalAccessError::new));
        return ResponseEntity.status(200).body(posts);
    }
}
