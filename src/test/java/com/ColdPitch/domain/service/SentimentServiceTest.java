package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.entity.dto.sentiment.SentimentResponseDto;
import com.ColdPitch.domain.entity.dto.sentiment.SentimentResponseDto.Document;
import com.ColdPitch.domain.repository.CommentRepository;
import com.ColdPitch.domain.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SentimentServiceTest {

    @InjectMocks
    private SentimentService sentimentAnalysisService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private SentimentApiClient sentimentApiClient;

    @Test
    public void testAnalyzePostSentiment() {
        // Given
        Long postId = 1L;
        Post post = Post.builder().id(postId).build();
        Comment comment1 = Comment.builder().text("싸늘하다. 가슴에 비수가 날아와 꽂힌다.").build();
        Comment comment2 = Comment.builder().text("하지만 걱정하지 마라. 손은 눈보다 빠르니까.").build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findAllByPostId(postId)).thenReturn(Arrays.asList(comment1, comment2));

        SentimentResponseDto responseDto = new SentimentResponseDto();
        Document document = new Document();
        Document.Sentiment sentiment = new Document.Sentiment();
        sentiment.setLabel("positive");
        document.setSentiment(sentiment);
        responseDto.setDocument(document);

        when(sentimentApiClient.callSentimentApi(any())).thenReturn(responseDto);


        // When
        sentimentAnalysisService.analyzeSentiment(postId);

        // Then
        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findAllByPostId(postId);
        verify(sentimentApiClient, times(1)).callSentimentApi(any());
    }
}