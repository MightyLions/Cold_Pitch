package com.ColdPitch.core.manager;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class PostFileManager extends AWSFileManager {
    private final String path = "post" + File.separator + ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Seoul")).toLocalDate().toString();

    public PostFileManager(AmazonS3 s3Client) {
        super(s3Client);
    }

    @Override
    public String upload(String string, MultipartFile multipartFile) {
        return super.upload(path, multipartFile);
    }

    @Override
    public boolean delete(String fileName) {
        return super.delete(fileName);
    }

}
