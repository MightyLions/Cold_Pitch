package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.user.CurState;
import com.ColdPitch.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ColdPitch.domain.entity.User;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터 베이스에서 찾을수 없습니다"));
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {
        GrantedAuthority grantedAuthorities = new SimpleGrantedAuthority(user.getUserType().toString());

        //유저 객체가 활성화 되어있다면 만들어서 리턴해준다.
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of(grantedAuthorities));
    }
}