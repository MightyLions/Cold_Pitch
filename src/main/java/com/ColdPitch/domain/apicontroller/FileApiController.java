package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.File;
import com.ColdPitch.domain.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileApiController {
    private final FileService fileService;

    @PostMapping("/upload")
    public File upload(@RequestBody MultipartFile[] multipartFiles) {
        return File.builder().build();
    }
}
