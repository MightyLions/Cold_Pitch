package com.ColdPitch.domain.entity.dto.sentiment;

import lombok.Data;

@Data
public class SentimentResponseDto {
    private Document document;

    @Data
    public static class Document {
        private Sentiment sentiment;
        private Confidence confidence;

        @Data
        public static class Sentiment {
            private String label;   // 감정: negative, positive
        }

        @Data
        public static class Confidence {
            private double negative;
            private double positive;
        }
    }
}
