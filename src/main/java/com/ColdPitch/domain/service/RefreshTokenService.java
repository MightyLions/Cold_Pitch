package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.dto.jwt.RefreshToken;
import com.ColdPitch.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findKey(String key) {
        return refreshTokenRepository.findByKey(key);
    }
}
