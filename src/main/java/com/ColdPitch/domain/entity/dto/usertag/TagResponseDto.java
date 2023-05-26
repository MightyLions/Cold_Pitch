package com.ColdPitch.domain.entity.dto.usertag;

import com.ColdPitch.domain.entity.Tag;
import com.ColdPitch.domain.entity.UserTag;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TagResponseDto {
    String name;
    String description;

    public static TagResponseDto of(Tag tag) {
        return new TagResponseDto(tag.getTagName(), tag.getDescription());
    }

    public static TagResponseDto of(UserTag userTag) {
        return new TagResponseDto(userTag.getTag().getTagName(), userTag.getTag().getDescription());
    }
}
