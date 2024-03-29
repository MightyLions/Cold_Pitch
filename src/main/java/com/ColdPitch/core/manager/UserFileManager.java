package com.ColdPitch.core.manager;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class UserFileManager extends AWSFileManager {

    private final String path = "profile" + File.separator + ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Seoul")).toLocalDate().toString();

    public UserFileManager(AmazonS3 s3Client) {
        super(s3Client);
    }

    @Override
    public String upload(String string, MultipartFile multipartFile) {
        validateFileExists(multipartFile);
        return super.upload(path, multipartFile);
    }

    @Override
    public boolean delete(String fileName) {
        return super.delete(fileName);
    }

    public void validateFileExists(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File does not exist or is empty.");
        }

        String contentType = file.getContentType();
        if (StringUtils.isEmpty(contentType)) {
            throw new IllegalArgumentException("File content type is empty.");
        }

        MediaType mediaType = MediaType.parseMediaType(contentType);
        if (!mediaType.equals(MediaType.IMAGE_PNG) && !mediaType.equals(MediaType.IMAGE_JPEG)) {
            throw new IllegalArgumentException("Only PNG, JPEG, and JPG files are allowed.");
        }
    }

}
