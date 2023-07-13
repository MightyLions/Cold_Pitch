package com.ColdPitch.core.manager;


import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.handler.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public abstract class AbstractFileManager implements FileManager{
    private final String path;


    public AbstractFileManager(String path) {
        this.path = path;

        if (!hasDir(path)){
            mkdirs(new File(path));
        }
    }

    @Override
    public String upload(String url, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        try{
            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            String uuid = UUID.randomUUID() + "." + ext;
            String filePath = path + File.separator + uuid;
            file.transferTo(new File(filePath));

            log.info("file upload success = {}", filePath);
            return uuid;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
    }

    @Override
    public boolean delete(String fileName) {
        File file = new File(path + File.separator + fileName);
        if (file.exists()) {
            if(file.delete()) {
                log.info("success file delete = {}", file.getAbsolutePath());
                return true;
            }
        }
        log.info("fail file delete = {}", file.getAbsolutePath());
        return false;
    }

    private static boolean hasDir(String path) {
        return new File(path).exists();
    }

    private static void mkdirs(File file) {
        file.mkdirs();
    }
}
