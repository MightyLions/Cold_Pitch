package com.ColdPitch.core.manager;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SolutionFileManager extends AWSFileManager{

    public SolutionFileManager(AmazonS3 s3Client) {
        super(s3Client);
    }

    public String upload(MultipartFile multipartFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("/solution");
        String timeNow = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Seoul")).toLocalDate().toString();
        sb.append("/");
        sb.append(timeNow);

        return super.upload(sb.toString(), multipartFile);
    }
}
