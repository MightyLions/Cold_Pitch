package com.ColdPitch.domain.entity.dto.sentiment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SentimentClassifiedDto {
    private List<String> positiveSentences;
    private List<String> negativeSentences;
}
