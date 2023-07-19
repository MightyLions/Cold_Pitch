package com.ColdPitch.domain.entity.dto.sentiment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SentimentRequestDto {
    private String content;

    @Override
    public String toString() {
        return "SentimentRequestDto{" +
                "content='" + content + '\'' +
                '}';
    }
}
