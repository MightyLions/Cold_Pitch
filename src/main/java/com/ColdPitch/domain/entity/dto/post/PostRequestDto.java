package com.ColdPitch.domain.entity.dto.post;

import com.ColdPitch.domain.entity.post.Category;
import com.ColdPitch.domain.entity.post.PostState;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PostRequestDto {
    private Long id;
    private PostState status;
    @NotNull
    private String title;
    @NotNull
    private String text;
    @NotNull
    private Category category;
}

