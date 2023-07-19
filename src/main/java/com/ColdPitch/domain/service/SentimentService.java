package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.dto.sentiment.*;
import com.ColdPitch.domain.repository.CommentRepository;
import com.ColdPitch.domain.repository.PostRepository;
import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.handler.ErrorCode;
import com.ColdPitch.utils.ValueThenKeyComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SentimentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final SentimentApiClient sentimentAnalysisApiClient;
    private final IWordAnalysisService wordAnalysisService;

    //  주어진 문자열을 기반으로 word cloud를 만드는 메소드
    public SentimentClassifiedResponseDto getAnalyzedSentimentAndWordMapByLiteralString(String string) {
        SentimentRequestDto sentimentRequestDto = new SentimentRequestDto(string);
        SentimentResponseDto sentimentResponseDto = sentimentAnalysisApiClient.callSentimentApi(sentimentRequestDto);
        SentimentClassifiedDto sentimentClassifiedDto = classifySentiments(sentimentResponseDto);

        List<List<Map.Entry<String, Integer>>> mapEntriesList = getSentimentAnalysedList(sentimentClassifiedDto);

        return new SentimentClassifiedResponseDto(
                mapEntriesList.get(0),
                mapEntriesList.get(1)
        );
    }

    //  postId를 기반으로 word cloud를 만드는 메소드
    public SentimentClassifiedResponseDto getAnalyzedSentimentAndWordMapByPostId(Long postId) {
        SentimentClassifiedDto sentimentClassifiedDto = analyzeAndClassifySentiment(postId);

        List<List<Map.Entry<String, Integer>>> mapEntriesList = getSentimentAnalysedList(sentimentClassifiedDto);

        return new SentimentClassifiedResponseDto(
                mapEntriesList.get(0),
                mapEntriesList.get(1)
        );
    }

    private SentimentClassifiedDto analyzeAndClassifySentiment(Long postId) {
        SentimentResponseDto responseDto = analyzeSentiment(postId);
        return classifySentiments(responseDto);
    }

    // 감정 평가 API 호출
    private SentimentResponseDto analyzeSentiment(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new CustomException(ErrorCode.POST_NOT_EXISTS);
        }

        List<Comment> comments = commentRepository.findAllByPostId(postId);

        if (comments == null || comments.isEmpty()) {
            throw new CustomException(ErrorCode.COMMENT_NOT_EXISTS);
        }

        // 여러 댓글을 하나의 문자열로 병합
        String commentsText = comments.stream()
                .map(Comment::getText)
                .collect(Collectors.joining(" "));

        SentimentRequestDto requestDto = new SentimentRequestDto(commentsText);
        return sentimentAnalysisApiClient.callSentimentApi(requestDto);
    }

    // API 평가 결과를 긍정/부정 두가지 리스트로 분류
    private SentimentClassifiedDto classifySentiments(SentimentResponseDto responseDto) {
        List<String> positiveSentences = new ArrayList<>();
        List<String> negativeSentences = new ArrayList<>();

        for (SentenceDto sentence : responseDto.getSentences()) {
            if ("positive".equals(sentence.getSentiment())) {
                positiveSentences.add(sentence.getContent());
            } else if ("negative".equals(sentence.getSentiment())) {
                negativeSentences.add(sentence.getContent());
            }
        }

        return new SentimentClassifiedDto(positiveSentences, negativeSentences);
    }

    //  분리된 긍부정 반응을 바탕으로 word cloud를 만드는 메소드
    private List<List<Map.Entry<String, Integer>>> getSentimentAnalysedList(SentimentClassifiedDto classifiedDto) {
        List<Map.Entry<String, Integer>> positiveWordList = new ArrayList<>(wordAnalysisService
                .doWordAnalysis(classifiedDto.getPositiveSentences().toString()).entrySet());
        List<Map.Entry<String, Integer>> negativeWordList = new ArrayList<>(wordAnalysisService
                .doWordAnalysis(classifiedDto.getNegativeSentences().toString()).entrySet());

        Collections.sort(positiveWordList, new ValueThenKeyComparator<>());
        Collections.sort(negativeWordList, new ValueThenKeyComparator<>());

        return List.of(
                positiveWordList,
                negativeWordList
        );
    }
}

