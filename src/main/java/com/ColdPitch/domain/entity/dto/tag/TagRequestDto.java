package com.ColdPitch.domain.entity.dto.tag;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TagRequestDto {
    @NotBlank
    String tagName;
    @NotBlank
    String description;
}
