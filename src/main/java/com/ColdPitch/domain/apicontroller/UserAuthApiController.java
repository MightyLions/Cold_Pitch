package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.config.security.JwtConfig;
import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.domain.entity.dto.user.CompanyRequestDto;
import com.ColdPitch.domain.entity.dto.user.LoginDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.dto.user.UserResponseDto;
import com.ColdPitch.domain.entity.user.UserType;
import com.ColdPitch.domain.repository.UserRepository;
import com.ColdPitch.domain.service.UserService;
import com.ColdPitch.jwt.JwtFilter;
import com.ColdPitch.utils.RandomUtil;
import com.ColdPitch.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class UserAuthApiController {
    private final UserService userService;
    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;

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
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        TokenDto loginResponse = userService.login(loginDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + loginResponse.getAccessToken());
        String cookie = "refreshToken=" + loginResponse.getRefreshToken() + "; Path=/; Max-Age=" + jwtConfig.getRefreshExpirationTime() + "; HttpOnly; readonly";
        httpHeaders.add("Set-Cookie", cookie);
        return new ResponseEntity<>(loginResponse, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "리프레시 토큰 삭제")
    public ResponseEntity<Void> logout() {
        userService.logout(SecurityUtil.getCurrentUserEmail().orElseThrow(IllegalAccessError::new));

        HttpHeaders httpHeaders = new HttpHeaders();
        String cookie = "refreshToken= ; Path=/; Max-Age=0; HttpOnly; readonly";
        httpHeaders.add("Set-Cookie", cookie);
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/dummy")
    @Operation(summary = "USER 더미데이터 생성", description = "default USER, 원하는 경우 ADMIN 입력")
    public ResponseEntity<List<UserResponseDto>> makeDummyUser(
            @Valid
            @RequestParam(defaultValue = "USER", name = "userType(Default : USER)") UserType userType,
            @RequestParam(defaultValue = "5", name = "size(Default : 5)") int size) {
        List<UserResponseDto> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            long rand = RandomUtil.getRandom(Integer.MAX_VALUE);
            list.add(userService.signUpUser(new UserRequestDto("nick" + rand, "name" + rand, "password" + rand, "eamil" + rand, "phone" + rand, userType)));
        }
        return ResponseEntity.status(200).body(list);
    }

    @Transactional
    @GetMapping("/testUserToken")
    @Operation(summary = "USER 테스트 토큰 생성", description = "testNick, testName,testPass,TestEmail@naver.com의 User")
    public ResponseEntity<String> makeDummyUserToken() {
        UserRequestDto urd = new UserRequestDto("testNick", "testName", "testPass", "testEamil@naver.com", "010-1234-1234", UserType.USER);
        return getStringResponseEntity(urd);
    }

    @Transactional
    @GetMapping("/testAdminToken")
    @Operation(summary = "ADMIN 테스트 토큰 생성", description = "testANick, testAName,testAPass,TestAEmail@naver.com의 User")
    public ResponseEntity<String> makeDummyAdminToken() {
        UserRequestDto urd = new UserRequestDto("testANick", "testAName", "testAPass", "testAEamil@naver.com", "010-5678-5678", UserType.ADMIN);
        return getStringResponseEntity(urd);
    }

    @NotNull
    private ResponseEntity<String> getStringResponseEntity(UserRequestDto urd) {
        userRepository.deleteByEmail(urd.getEmail());
        userService.signUpUser(urd);
        TokenDto loginResponse = userService.login(new LoginDto(urd.getEmail(), urd.getPassword()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + loginResponse.getAccessToken());
        String cookie = "refreshToken=" + loginResponse.getRefreshToken() + "; Path=/; Max-Age=" + jwtConfig.getRefreshExpirationTime() + "; HttpOnly; readonly";
        httpHeaders.add("Set-Cookie", cookie);
        return new ResponseEntity<>("Bearer " + loginResponse.getAccessToken(), httpHeaders, HttpStatus.OK);
    }
}
