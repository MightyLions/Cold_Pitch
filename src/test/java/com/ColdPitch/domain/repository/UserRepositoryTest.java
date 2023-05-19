package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.user.CurState;
import com.ColdPitch.domain.repository.UserRepository;
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
    public void 유저객체_생성_테스트() {
        //given
        User user = User.builder().name("test")
                .password("test")
                .email("test@naver.com")
                .curState(CurState.LIVE)
                .nickname("nick")
                .phoneNumber("010-1234-1234").build();

        //when
        userRepository.save(user);

        //then
        User user2 = userRepository.findById(user.getId()).get();
        assertThat(user.getEmail()).isEqualTo(user2.getEmail());
    }

    @Test
    public void 유저객체_전체_조회() {
        //given
        for (int i = 0; i < 10; i++) {
            userRepository.save(User.builder().name("test" + i)
                    .nickname("test" + i)
                    .password("test" + i)
                    .email("bo" + i + "@n.com")
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