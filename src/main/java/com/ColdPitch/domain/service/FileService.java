package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.File;
import com.ColdPitch.domain.entity.dto.file.FileUploadRequest;
import com.ColdPitch.domain.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final FileRepository fileRepository;

    private File uploadFile() {
        return File.builder().build();
    }

    public File save(FileUploadRequest fileUploadRequest) {
//        fileRepository.save();
        return File.builder().build();
    }
}
