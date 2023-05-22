package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.jwt.RefreshToken;
import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.domain.entity.dto.jwt.TokenRequestDto;
import com.ColdPitch.domain.entity.dto.user.LoginDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.dto.user.UserResponseDto;
import com.ColdPitch.domain.entity.user.CurState;
import com.ColdPitch.domain.entity.user.UserType;
import com.ColdPitch.domain.repository.RefreshTokenRepository;
import com.ColdPitch.domain.repository.UserRepository;
import com.ColdPitch.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public UserResponseDto signup(UserRequestDto userRequestDto) {
        //TODO 유저 이메일, 닉네임 중복 확인 ( 이메일 형식, 전화번호 형식 확인 부분도 추가해야함)

        User user = User.builder()
                .name(userRequestDto.getName())
                .nickname(userRequestDto.getNickname())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .email(userRequestDto.getEmail())
                .phoneNumber(userRequestDto.getPhoneNumber())
                .userType(UserType.of(userRequestDto.getUserType()))
                .curState(CurState.LIVE)
                .nickname(userRequestDto.getNickname()).build();

        return UserResponseDto.of(userRepository.save(user));
    }

    @Transactional
    public TokenDto login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        //RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }


    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("유효하지 않은 Refresh Token 입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName()).orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 정보가 유저 정보가 일치하지 않습니다.");
        }

        //새로운 토큰 발급
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        return tokenDto;
    }

    @Transactional
    public void logout(String nowLoginEmail) {
        refreshTokenRepository.deleteByKey(nowLoginEmail);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Transactional
    public UserResponseDto updateProfile(@ApiIgnore String userEmail, UserRequestDto userRequestDto) {
        //TODO 수정시에 validation 확인 ( 로그인한 사람이 본인이 맞는지 확인 )
        User user = userRepository.findOneWithAuthoritiesByEmail(userEmail).orElseThrow();
        user.updateProfile(userRequestDto);
        user.updatePassword(passwordEncoder.encode(userRequestDto.getPassword()));
        return UserResponseDto.of(user);
    }

    public List<UserResponseDto> findAllUser() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDto::of)
                .collect(Collectors.toList());

    }

    public UserResponseDto findByNickName(String nickname) {
        Optional<User> find = userRepository.findByNickname(nickname);
        if (find.isPresent()) {
            return UserResponseDto.of(find.get());
        }
        throw new RequestRejectedException("없는 nickname 입니다");
    }

    @Transactional
    public void deleteUser(String email) {
        logout(email); //리프레시 토큰을 삭제한다.
        userRepository.deleteByEmail(email);
    }
}
