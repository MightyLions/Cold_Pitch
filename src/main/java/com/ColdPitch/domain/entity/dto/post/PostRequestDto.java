package com.ColdPitch.domain.entity.dto.post;

import com.ColdPitch.domain.entity.post.PostState;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostRequestDto {
    private Long id;
    private PostState status;
    @NotNull
    private String title;
    @NotNull
    private String text;
    @NotNull
    private String category;
}

// status는 따로 API를 분리
