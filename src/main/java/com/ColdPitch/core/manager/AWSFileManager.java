package com.ColdPitch.core.manager;

import com.ColdPitch.core.factory.YamlLoadFactory;
import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.handler.ErrorCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@PropertySource(value = {"classpath:application-s3.yml"}, factory = YamlLoadFactory.class)
@Slf4j
public abstract class AWSFileManager implements FileManager {
    private final AmazonS3 s3Client;
    @Value(value = "${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String upload(String path, MultipartFile multipartFile) {
        existFile(multipartFile);

        ObjectMetadata objectMetadata = metaDataInstance(multipartFile);
        String fileUrl = makeFileUrl(path, multipartFile);

        try (InputStream is = multipartFile.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucketName, fileUrl, is, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicReadWrite));


            return fileUrl;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
    }


    private static class getExtension {
        public final String fileName;
        public final String ext;

        public getExtension(String fileName, String ext) {
            this.fileName = fileName;
            this.ext = ext;
        }
    }

    @Override
    public boolean delete(String fileName) {
        if (!s3Client.doesObjectExist(bucketName, fileName)) {
            throw new AmazonS3Exception("Object " + fileName + " 존재하지 않는 이미지 입니다!");
        }
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return true;
    }

    @Override
    public void download(String filePath, String fileName) {

    }

    @Override
    public String read(String path) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, s3Client.getBucketLocation(bucketName), path);
    }

    private static String makeFileUrl(String path, MultipartFile multipartFile) {
        String extension = getExtension(multipartFile);
        String fileName = UUID.randomUUID() + "." + extension;
        return path + "/" + fileName;
    }

    private static String getExtension(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private static void existFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new CustomException(ErrorCode.FILE_NOT_EXIST);
        }
    }

    private static ObjectMetadata metaDataInstance(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        return objectMetadata;
    }

}
