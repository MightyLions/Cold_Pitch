package com.ColdPitch.domain.entity.dto.sentiment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SentimentResponseDto {
    private DocumentDto document;
    private List<SentenceDto> sentences;

    @Override
    public String toString() {
        return "SentimentResponseDto{" +
                "document=" + document +
                ", sentences=" + sentences +
                '}';
    }
}
