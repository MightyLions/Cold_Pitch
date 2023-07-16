package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Tag;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.UserTag;
import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationDto;
import com.ColdPitch.domain.entity.dto.user.CompanyRequestDto;
import com.ColdPitch.domain.entity.dto.user.CompanyResponseDto;
import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.dto.usertag.TagRequestDto;
import com.ColdPitch.domain.entity.user.UserType;
import com.ColdPitch.domain.repository.UserTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@Slf4j
class UserTagServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserTagService userTagService;
    @Autowired
    private UserTagRepository userTagRepository;
    @MockBean
    private CompanyRegistrationValidator companyRegistrationValidator;

    private User user;
    private User company, company1;
    private Tag tag1, tag2, tag3, tag4;

    @Test
    @DisplayName("유저회원 테그 입력 기능 확인")
    public void addUserTag() {
        //given

        // when
        userTagService.setTag(new TagRequestDto(List.of("tag1", "tag2")), user);

        //then
        List<UserTag> allUserTags = userTagRepository.findAll();
        assertThat(allUserTags.size()).isEqualTo(2);
        assertThat(allUserTags.get(0).getTag()).isEqualTo(tag1);
        assertThat(allUserTags.get(0).getUser()).isEqualTo(user);
        assertThat(allUserTags.get(1).getTag()).isEqualTo(tag2);
        assertThat(allUserTags.get(1).getUser()).isEqualTo(user);
    }


    @Test
    @DisplayName("태그를 기반으로 회사를 찾는 기능 or - other things")
    public void findCompanyByTagORTest1() {
        //given
        userTagService.setTag(new TagRequestDto(List.of("tag1", "tag2")), company);
        userTagService.setTag(new TagRequestDto(List.of("tag2", "tag3")), company1);
        log.info("COMPANY = {}", userTagService.findMyTag(company));
        log.info("COMPANY1 = {}", userTagService.findMyTag(company1));


        // when
        List<CompanyResponseDto> find = userTagService.findCompanyByEachAllTags(new TagRequestDto(List.of("tag1", "tag3")));

        //then
        log.info("{}", find);
        assertThat(find.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("태그를 기반으로 회사를 찾는 기능 or- same things")
    public void findCompanyByTagORTest2() {
        //given
        userTagService.setTag(new TagRequestDto(List.of("tag1", "tag2")), company);
        userTagService.setTag(new TagRequestDto(List.of("tag2", "tag3")), company1);

        // when
        List<CompanyResponseDto> find = userTagService.findCompanyByEachAllTags(new TagRequestDto(List.of("tag2")));

        //then
        log.info("{}", find);
        assertThat(find.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("태그를 기반으로 회사를 찾는 기능 or - empty")
    public void findCompanyByTagORTest3() {
        //given
        userTagService.setTag(new TagRequestDto(List.of("tag1", "tag2")), company);

        // when
        List<CompanyResponseDto> find = userTagService.findCompanyByEachAllTags(new TagRequestDto(List.of()));

        //then
        assertThat(find.size()).isEqualTo(0);
    }


    @BeforeEach
    public void initUser() {
        //mocking
        when(companyRegistrationValidator.validateCompanyRegistration(any())).thenReturn(true);

        UserRequestDto userRequestDto = new UserRequestDto("nickname", "name", "password", "email@naver.com", "010-7558-2452", UserType.USER);
        UserRequestDto companyRequestDto = new UserRequestDto("Bnickname", "Bname", "Bpassword", "Bemail@naver.com", "010-3333-2452", UserType.BUSINESS);
        UserRequestDto companyRequestDto1 = new UserRequestDto("Bnickname1", "Bname1", "Bpassword1", "Bemail@naver.com1", "010-3333-2451", UserType.BUSINESS);
        CompanyRegistrationDto companyRegistrationDto = new CompanyRegistrationDto("12345678", "20230526", "test", "test", "(주)테스트", "0000000000000", "test", "test", "test", "test");
        CompanyRegistrationDto companyRegistrationDto1 = new CompanyRegistrationDto("98765432", "20230526", "test1", "test1", "(주)테스트1", "0000000000000", "test1", "test1", "test1", "test1");


        userService.signUpUser(userRequestDto);
        userService.signUpCompany(new CompanyRequestDto(companyRequestDto, companyRegistrationDto));
        userService.signUpCompany(new CompanyRequestDto(companyRequestDto1, companyRegistrationDto1));

        user = userService.findUserByEmail(userRequestDto.getEmail());
        company = userService.findUserByEmail(companyRequestDto.getEmail());
        company1 = userService.findUserByEmail(companyRequestDto1.getEmail());

        tag1 = tagService.createTag(new com.ColdPitch.domain.entity.dto.tag.TagRequestDto("tag1", "tag1"));
        tag2 = tagService.createTag(new com.ColdPitch.domain.entity.dto.tag.TagRequestDto("tag2", "tag2"));
        tag3 = tagService.createTag(new com.ColdPitch.domain.entity.dto.tag.TagRequestDto("tag3", "tag3"));
        tag4 = tagService.createTag(new com.ColdPitch.domain.entity.dto.tag.TagRequestDto("tag4", "tag4"));
    }

}

