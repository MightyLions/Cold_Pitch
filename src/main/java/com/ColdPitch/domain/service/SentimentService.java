package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.dto.sentiment.SentimentRequestDto;
import com.ColdPitch.domain.entity.dto.sentiment.SentimentResponseDto;
import com.ColdPitch.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SentimentService {

    private final CommentRepository commentRepository;
    private final SentimentApiClient sentimentAnalysisApiClient;

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
}

