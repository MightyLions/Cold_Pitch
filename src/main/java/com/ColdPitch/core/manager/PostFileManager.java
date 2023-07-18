package com.ColdPitch.core.manager;

import com.amazonaws.services.s3.AmazonS3;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class PostFileManager extends AWSFileManager{

    public PostFileManager(AmazonS3 s3Client) {super(s3Client);}
    @Override
    public String upload(String path, MultipartFile multipartFile) {

        StringBuilder sb = new StringBuilder();
        sb.append("/post/");
        sb.append(path);
        sb.append("/");
        String timeNow = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Seoul")).toLocalDate().toString();
        sb.append(timeNow);

        return super.upload(sb.toString(), multipartFile);
    }


    @Override
    public boolean delete(String fileName) {
        return super.delete(fileName);
    }

}
