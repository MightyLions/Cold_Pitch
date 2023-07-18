/**
 * @project ColdPitch
 * @author ARA
 * @since 2023-07-19 AM 3:41
 */

package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.dto.sentiment.SentimentClassifiedResponseDto;
import com.ColdPitch.domain.entity.dto.sentiment.SentimentLiteralStringRequestDto;
import com.ColdPitch.domain.service.SentimentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("sentiment/{postId}")
@Validated
public class SentimentApiController {
    private final SentimentService sentimentService;

    @GetMapping
    @Operation(summary = "post의 댓글을 종합해 감정분석을 진행하는 메소드",
            description = "주어진 postId 값을 바탕으로 댓글들의 감정을 긍정, 부정으로 분석하고 빈도수를 리턴하는 메소드")
    public SentimentClassifiedResponseDto getSentimentAnalysedByPostId(
            @PathVariable("postId") @Min(1) @Max(Integer.MAX_VALUE) Long postId
    ) {
        return sentimentService.getAnalyzedSentimentAndWordMapByPostId(postId);
    }

    @PostMapping
    @Operation(summary = "감정분석을 테스트 하는 메소드",
            description = "주어진 message 값으로 감정 분석을 진행하는 메소드, http://hangul.thefron.me/ 위 사이트에서 값을 복사해서 테스트 진행하기")
    public SentimentClassifiedResponseDto getSentimentAnalysedByLiteralString(
            @RequestBody SentimentLiteralStringRequestDto requestDto
            ) {
        return sentimentService.getAnalyzedSentimentAndWordMapByLiteralString(requestDto.getMessage());
    }
}
