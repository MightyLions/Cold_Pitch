package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.dto.file.FileUploadResponse;

import java.util.List;

public interface FileRepositoryCustom {
//    List<FileUploadResponse> findAll();

    List<FileUploadResponse> findByPath(String path);
}
