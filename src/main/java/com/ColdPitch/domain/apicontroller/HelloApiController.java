package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.Hello;
import com.ColdPitch.domain.service.HelloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "test operation", description = "hello api test", tags = {"tags", "tags2"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    public ResponseEntity hello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/hello")
    public ResponseEntity save(Hello hello) {
        Hello save = helloService.save(hello);
        return ResponseEntity.ok(save);
    }

}
