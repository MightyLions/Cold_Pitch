package com.ColdPitch.domain.entity.dto.sentiment;

import lombok.Data;

import java.util.List;

@Data
public class SentimentResponseDto {
    private Document document;
    private List<Sentence> sentences;

    @Data
    public static class Document {
        private String sentiment;
        private ConfidenceScores confidence;
    }

    @Data
    public static class Sentence {
        private String sentiment;
        private String content;
        private ConfidenceScores confidence;
    }

    @Data
    public static class ConfidenceScores {
        private double negative;
        private double positive;
        private double neutral;
    }
}
