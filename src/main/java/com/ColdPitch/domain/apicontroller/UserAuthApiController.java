package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.User;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth") // 권한 인증 관련 api 여기에서 모두 관리.
public class UserAuthApiController {
    private final UserService userService;

    @PostMapping(value = "/signup")
    @Operation(summary = "회원가입", description = "회원 가입 API")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto userRequestDto) {
        log.info("회원가입 시도:{} ", userRequestDto);
        //회원 가입부분
        User savedMember = userService.signup(userRequestDto);

        if (UserType.of(userRequestDto.getUserType()) == UserType.BUSINESS) {
            //TODO 기업회원인 경우에만 레지스트레이션 저장할 부분 추가
        }

        //TODO  유저안에서 처리 해도될지 이대로 할지
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(savedMember.getId())
                .name(savedMember.getName())
                .nickname(savedMember.getNickname())
                .email(savedMember.getEmail())
                .phoneNumber(savedMember.getPhoneNumber())
                .userType(savedMember.getUserType())
                .curState(savedMember.getCurState())
                .build();

        log.info("회원가입 완료:{}", userResponseDto);
        return ResponseEntity.status(200).body(userResponseDto);
    }



    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        TokenDto loginResponse = userService.login(loginDto);
        return ResponseEntity.status(200).body(loginResponse);
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재 발급")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.reissue(tokenRequestDto));
    }

}
