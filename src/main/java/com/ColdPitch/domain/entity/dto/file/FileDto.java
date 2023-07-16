package com.ColdPitch.domain.entity.dto.file;

import com.ColdPitch.domain.entity.File;
import com.ColdPitch.domain.entity.FileStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDto {
    private String name;
    private String path;
    private Long size;
    private FileStatus status;

    public static File toEntity(FileDto fileDto) {
        return File.builder()
                .name(fileDto.name)
                .path(fileDto.path)
                .size(fileDto.size)
                .status(FileStatus.C)
                .build();
    }
}
