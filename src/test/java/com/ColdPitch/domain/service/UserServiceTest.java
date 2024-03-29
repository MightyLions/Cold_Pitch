package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationDto;
import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.domain.entity.dto.user.CompanyRequestDto;
import com.ColdPitch.domain.entity.dto.user.LoginDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.user.CurState;
import com.ColdPitch.domain.entity.user.UserType;
import com.ColdPitch.domain.repository.UserRepository;
import com.ColdPitch.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@Transactional
@Slf4j
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @MockBean
    private CompanyRegistrationValidator companyRegistrationValidator;

    private UserRequestDto userRequestDto;

    @BeforeEach
    void init() {
        userRequestDto = new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", UserType.USER);
        //벨리데이터 모킹
        when(companyRegistrationValidator.validateCompanyRegistration(any())).thenReturn(true);
    }

    @Test
    @DisplayName("유저회원 회원가입을 확인한다. ")
    public void signupUser() {
        //given

        // when
        userService.signUpUser(userRequestDto);

        //then
        User savedUser = userService.findUserByEmail(userRequestDto.getEmail());
        assertThat(savedUser.getEmail()).isEqualTo("email@naver.com");
        assertThat(savedUser.getPhoneNumber()).isEqualTo("010-7558-2452");
        assertThat(savedUser.getUserType().name()).isEqualTo("USER");
        assertThat(savedUser.getNickname()).isEqualTo("nickname");
        assertThat(savedUser.getName()).isEqualTo("name");
        assertTrue(checkPassword(savedUser.getPassword(), "password"));
    }


    @Test
    @DisplayName("기업회원 회원가입을 확인한다. ")
    public void signupCompany() {
        //given
        UserRequestDto companyRequestDto = new UserRequestDto("Bnickname", "Bname", "Bpassword", "Bemail@naver.com", "010-7558-2452", UserType.BUSINESS);
        CompanyRegistrationDto companyRegistrationDto = new CompanyRegistrationDto("12345678", "20230526", "test", "test", "(주)테스트", "0000000000000", "부동산업", "부동산중개업", "test", "test");

        // when
        userService.signUpCompany(new CompanyRequestDto(companyRequestDto, companyRegistrationDto));

        //then
        User savedUser = userService.findUserByEmail(companyRequestDto.getEmail());
        assertThat(savedUser.getEmail()).isEqualTo(companyRequestDto.getEmail());
        assertThat(savedUser.getPhoneNumber()).isEqualTo(companyRequestDto.getPhoneNumber());
        assertThat(savedUser.getUserType()).isEqualTo(companyRequestDto.getUserType());
        assertThat(savedUser.getNickname()).isEqualTo(companyRequestDto.getNickname());
        assertThat(savedUser.getName()).isEqualTo(companyRequestDto.getName());
        assertTrue(checkPassword(savedUser.getPassword(), companyRequestDto.getPassword()));
        assertThat(savedUser.getCompanyRegistration().getB_nm()).isEqualTo(companyRegistrationDto.getB_nm());
    }

    @Test
    @DisplayName("유저 회원 로그인을 성공을 확인한다.")
    public void loginTestsuite() {
        //given
        userService.signUpUser(userRequestDto);

        //when
        LoginDto loginDto = new LoginDto("email@naver.com", "password");
        TokenDto login = userService.login(loginDto);

        //then ( 생성된 토큰의 유효성을 확인한다. )
        assertThat(tokenProvider.getAuthentication(login.getAccessToken()).getName()).isEqualTo("email@naver.com");
    }

    @Test
    @DisplayName("유저 회원 로그인을 실패를 확인한다.")
    public void loginFailTestsuite() {
        //given
        userService.signUpUser(userRequestDto);

        //when
        LoginDto loginDto = new LoginDto("email@naver.com", "pass");
        Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.login(loginDto);
        });

    }

    @Test
    @DisplayName("유저 로그아웃을 확인한다.")
    public void logoutTestsuite() {
        //given
        userService.signUpUser(userRequestDto);
        LoginDto loginDto = new LoginDto("email@naver.com", "password");
        TokenDto login = userService.login(loginDto);

        //when
        userService.logout(userRequestDto.getEmail());

        //then ( 생성된 토큰의 유효성을 확인한다. )
        assertThat(refreshTokenService.findKey(userRequestDto.getEmail())).isEqualTo(java.util.Optional.empty());
    }

    @Test
    @DisplayName("유저 탈퇴를 확인한다.")
    public void deletedTestsuite() {
        //given
        userService.signUpUser(userRequestDto);

        //when
        userService.deleteUser(userRequestDto.getEmail());

        //then
        List<User> users = userRepository.findUserByEmailIncludeDeletedUser(userRequestDto.getEmail()).orElseThrow();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getCurState()).isEqualTo(CurState.DELETED); //컬럼이 삭제인지 확인
        assertThat(refreshTokenService.findKey(userRequestDto.getEmail())).isEmpty();//refreshtoken 삭제 확인
    }

    @Test
    @DisplayName("유저 탈퇴시 조회를 확인한다.")
    public void deletedSelectTestsuite() {
        //given
        userService.signUpUser(userRequestDto);

        //when
        userService.deleteUser(userRequestDto.getEmail());

        //then
        assertThat(userRepository.findByEmail(userRequestDto.getEmail())).isEmpty();//조회시 없음을 확인
    }

    private boolean checkPassword(String password, String exPassword) {
        return passwordEncoder.matches(exPassword, password);
    }
}