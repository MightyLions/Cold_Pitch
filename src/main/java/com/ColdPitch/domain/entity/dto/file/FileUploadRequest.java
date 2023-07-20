package com.ColdPitch.domain.entity.dto.file;

import com.ColdPitch.domain.entity.FileStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequest {
    private String name;
    private String path;
    private Long size;
    private FileStatus status;
}
