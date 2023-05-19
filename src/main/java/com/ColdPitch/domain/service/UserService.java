package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.user.CurState;
import com.ColdPitch.domain.entity.user.UserType;
import com.ColdPitch.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User signup(UserRequestDto userRequestDto) {
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

        return userRepository.save(user);
    }

}
