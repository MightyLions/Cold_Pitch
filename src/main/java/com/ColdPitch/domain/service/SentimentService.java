package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.dto.sentiment.SentimentRequestDto;
import com.ColdPitch.domain.entity.dto.sentiment.SentimentResponseDto;
import com.ColdPitch.domain.entity.dto.sentiment.SentimentClassifiedDto;
import com.ColdPitch.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SentimentService {
    private final CommentRepository commentRepository;
    private final SentimentApiClient sentimentAnalysisApiClient;

    public SentimentClassifiedDto analyzeAndClassifySentiment(Long postId) {
        SentimentResponseDto responseDto = analyzeSentiment(postId);
        return classifySentiments(responseDto);
    }

    // 감정 평가 API 호출
    public SentimentResponseDto analyzeSentiment(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);

        // 여러 댓글을 하나의 문자열로 병합
        String commentsText = comments.stream()
                .map(Comment::getText)
                .collect(Collectors.joining(" "));

        SentimentRequestDto requestDto = new SentimentRequestDto(commentsText);
        requestDto.setContent(commentsText);
        return sentimentAnalysisApiClient.callSentimentApi(requestDto);
    }

    // API 평가 결과를 긍정/부정 두가지 리스트로 분류
    public SentimentClassifiedDto classifySentiments(SentimentResponseDto responseDto) {
        List<String> positiveSentences = new ArrayList<>();
        List<String> negativeSentences = new ArrayList<>();

        for (SentimentResponseDto.Sentence sentence : responseDto.getSentences()) {
            if ("positive".equals(sentence.getSentiment())) {
                positiveSentences.add(sentence.getContent());
            } else if ("negative".equals(sentence.getSentiment())) {
                negativeSentences.add(sentence.getContent());
            }
        }

        return new SentimentClassifiedDto(positiveSentences, negativeSentences);
    }
}

