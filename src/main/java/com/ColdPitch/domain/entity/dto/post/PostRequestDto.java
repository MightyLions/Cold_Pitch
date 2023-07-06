package com.ColdPitch.domain.entity.dto.post;

import com.ColdPitch.domain.entity.post.Category;
import com.ColdPitch.domain.entity.post.PostState;
import com.ColdPitch.validation.annotations.ValidEnum;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PostRequestDto {
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private Long id;
    private PostState status;
    @NotNull
    @javax.validation.constraints.NotNull
    @NotBlank
    private String title;
    @NotNull
    @javax.validation.constraints.NotNull
    @NotBlank
    private String text;
    @NotNull
    @javax.validation.constraints.NotNull
    @ValidEnum(enumClass = Category.class)
    private Category category;
}

// status는 따로 API를 분리
