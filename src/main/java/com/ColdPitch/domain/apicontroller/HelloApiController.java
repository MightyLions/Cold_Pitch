package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.Hello;
import com.ColdPitch.domain.service.HelloService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class HelloApiController {
    private final HelloService helloService;

    @GetMapping("/hello")
    public ResponseEntity hello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/hello")
    public ResponseEntity save(Hello hello) {
        Hello save = helloService.save(hello);
        return ResponseEntity.ok(save);
    }

}
