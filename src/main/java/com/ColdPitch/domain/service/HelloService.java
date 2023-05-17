package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Hello;
import com.ColdPitch.domain.repository.HelloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HelloService {
    private final HelloRepository helloRepository;

    public Hello save(Hello hello) {
        return helloRepository.save(hello);
    }
}
