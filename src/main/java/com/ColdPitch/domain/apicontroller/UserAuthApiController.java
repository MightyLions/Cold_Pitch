package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.domain.entity.dto.jwt.TokenRequestDto;
import com.ColdPitch.domain.entity.dto.user.CompanyRequestDto;
import com.ColdPitch.domain.entity.dto.user.LoginDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.dto.user.UserResponseDto;
import com.ColdPitch.domain.service.UserService;
import com.ColdPitch.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class UserAuthApiController {
    private final UserService userService;

    @PostMapping(value = "/user/signup")
    @Operation(summary = "유저 회원가입", description = "유저 회원 가입 API")
    public ResponseEntity<UserResponseDto> userSignup(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.signUpUser(userRequestDto);
        return ResponseEntity.status(200).body(userResponseDto);
    }

    @PostMapping(value = "/business/signup")
    @Operation(summary = "기업 회원가입", description = "기업 회원 가입 API")
    public ResponseEntity<UserResponseDto> businessSignup(@Valid @RequestBody CompanyRequestDto companyRequestDto) {
        UserResponseDto userResponseDto = userService.signUpCompany(companyRequestDto);
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
    public ResponseEntity<Void> logout() {
        userService.logout(SecurityUtil.getCurrentUserEmail().orElseThrow(IllegalAccessError::new));
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재 발급")
    public ResponseEntity<TokenDto> reissue(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.reissue(tokenRequestDto));
    }
}
