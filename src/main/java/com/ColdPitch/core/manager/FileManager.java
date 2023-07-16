package com.ColdPitch.core.manager;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileManager {
    String upload(String url, MultipartFile multipartFile) throws IOException;
    List<String> uploads(MultipartFile[] multipartFiles) throws IOException;
    boolean delete(String fileName);
    void download(String filePath, String fileName);
}
