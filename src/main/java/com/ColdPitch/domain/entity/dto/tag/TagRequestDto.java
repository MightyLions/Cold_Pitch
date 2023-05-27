package com.ColdPitch.domain.entity.dto.tag;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TagRequestDto {
    String tagName;
    String description;
}
