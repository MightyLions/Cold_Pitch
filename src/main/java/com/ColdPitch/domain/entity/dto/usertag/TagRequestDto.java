package com.ColdPitch.domain.entity.dto.usertag;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TagRequestDto {
    List<String> userTag;
}
