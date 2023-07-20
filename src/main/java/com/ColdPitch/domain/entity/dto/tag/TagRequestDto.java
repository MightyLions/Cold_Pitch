package com.ColdPitch.domain.entity.dto.tag;

import com.ColdPitch.domain.entity.Tag;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

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

    public Tag toEntity() {
        return Tag.builder()
                .tagName(this.getTagName().trim())
                .description(this.getDescription())
                .userTags(new ArrayList<>())
                .build();
    }
}
