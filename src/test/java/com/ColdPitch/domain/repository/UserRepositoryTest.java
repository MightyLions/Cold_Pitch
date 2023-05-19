package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.user.CurState;
import com.ColdPitch.domain.entity.user.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 객체 생성 테스트")
    public void makeTestUser() {
        //given
        User user = User.builder().name("test")
                .password("test")
                .email("test@naver.com")
                .curState(CurState.LIVE)
                .userType(UserType.USER)
                .nickname("nick")
                .phoneNumber("010-1234-1234").build();

        //when
        userRepository.save(user);

        //then
        User user2 = userRepository.findById(user.getId()).get();
        assertThat(user.getEmail()).isEqualTo(user2.getEmail());
    }

    @Test
    @DisplayName("유저 객체 전체 조회")
    public void findAllUser() {
        //given
        for (int i = 0; i < 10; i++) {
            userRepository.save(User.builder().name("test" + i)
                    .nickname("test" + i)
                    .password("test" + i)
                    .email("bo" + i + "@n.com")
                    .userType(UserType.USER)
                    .curState(CurState.LIVE)
                    .phoneNumber(String.valueOf(i)).build());
        }

        //when
        List<User> all = userRepository.findAll();
        //Collections.sort(all, (o1, o2) -> (int) (o1.getId() - o2.getId()));

        //then
        for (int i = 0; i < 10; i++) {
            //log.info("{}", all.get(i));
            assertThat(all.get(i).getName()).isEqualTo("test" + i);
        }
    }

}