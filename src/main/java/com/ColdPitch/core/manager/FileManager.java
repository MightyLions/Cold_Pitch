package com.ColdPitch.core.manager;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileManager {
    String upload(MultipartFile file) throws IOException;
    boolean delete(String fileName);

}
