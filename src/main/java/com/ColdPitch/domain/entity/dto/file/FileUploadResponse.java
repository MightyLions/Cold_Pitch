package com.ColdPitch.domain.entity.dto.file;

import com.ColdPitch.domain.entity.FileStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
//@AllArgsConstructor
public class FileUploadResponse {
    private String name;
    private String path;
    private Long size;
    private FileStatus status;

    @QueryProjection
    public FileUploadResponse(String name, String path, Long size, FileStatus status) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.status = status;
    }
}
