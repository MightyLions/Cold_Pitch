package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.domain.entity.dto.jwt.TokenRequestDto;
import com.ColdPitch.domain.entity.dto.user.LoginDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.dto.user.UserResponseDto;
import com.ColdPitch.domain.entity.user.UserType;
import com.ColdPitch.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class UserAuthApiController {
    private final UserService userService;

    @PostMapping(value = "/signup")
    @Operation(summary = "회원가입", description = "회원 가입 API")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.signup(userRequestDto);

        if (UserType.of(userRequestDto.getUserType()) == UserType.BUSINESS) {
            //TODO 기업회원인 경우에만 레지스트레이션 저장할 부분 추가
        }

        return ResponseEntity.status(200).body(userResponseDto);
    }


    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        TokenDto loginResponse = userService.login(loginDto);
        return ResponseEntity.status(200).body(loginResponse);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "리프레시 토큰 삭제")
    public ResponseEntity<Void> logout(@ApiIgnore Authentication authentication) {
        userService.logout(authentication.getName());
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재 발급")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.reissue(tokenRequestDto));
    }

}
