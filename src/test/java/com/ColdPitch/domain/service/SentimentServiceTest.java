/**
 * @project ColdPitch
 * @author ARA
 * @since 2023-07-15 AM 9:18
 */

package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.dto.sentiment.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
public class SentimentServiceTest {
    @Autowired
    private SentimentService sentimentService;
    @Autowired
    private SentimentApiClient sentimentApiClient;
    private final String shortInputString = "싸늘하다. 가슴에 비수가 날아와 꽂힌다.";
    private final String longInputString = "없는 가슴에 품으며, 아름답고 얼음이 봄바람을 찾아다녀도, " +
            "청춘의 살 것이다. 밥을 위하여, 창공에 뿐이다. 이상 눈에 내는 것이다. 인간의 그러므로 동산에는 " +
            "풀이 노년에게서 작고 끓는 열매를 고행을 사막이다. " +
            "인간에 하여도 것은 트고, 들어 인생에 청춘에서만 소리다.이것은 싸인 운다. " +
            "있는 어디 얼마나 사라지지 거친 인류의 꾸며 아니다. 쓸쓸한 창공에 할지라도 것이다." +
            "보라, 붙잡아 열락의 피에 아름답고 없으면 봄바람이다. 공자는 이상 보이는 운다. " +
            "뜨고, 못하다 긴지라 그와 말이다. 그러므로 생생하며, 창공에 불러 힘차게 품고 피다.";


    @Test
    @DisplayName("짧은 감정 분석 예시")
    public void sentimentTestcase1() throws Exception {
        SentimentRequestDto sentimentRequestDto = new SentimentRequestDto(shortInputString);
//        SentimentResponseDto expectedResponseDto = new SentimentResponseDto();
//        expectedResponseDto.setDocument(
//                new DocumentDto("negative",
//                        new ConfidenceScoresDto(
//                                0.6246391,
//                                0.0077638756,
//                                99.3676
//                        ))
//        );
//        expectedResponseDto.setSentences(
//                List.of(
//                        new SentenceDto[]{
//                                new SentenceDto(
//                                        "싸늘하다",
//                                        "negative",
//                                        new ConfidenceScoresDto(
//                                                0.0036366594,
//                                                0.0002274021,
//                                                0.9961359
//                                        )
//                                ),
//                                new SentenceDto(
//                                        "가슴에 비수가 날아와 꽂힌다.",
//                                        "negative",
//                                        new ConfidenceScoresDto(
//                                                0.071320035,
//                                                0.000704263,
//                                                0.9279757
//                                        )
//                                ),
//                        }
//                )
//        );

        SentimentResponseDto responseDto = sentimentApiClient.callSentimentApi(sentimentRequestDto);

        log.info(responseDto.toString());

//        Assertions.assertEquals(expectedResponseDto, responseDto);
    }

    @Test
    public void sentimentTestcase2() throws Exception{
        SentimentRequestDto sentimentRequestDto = new SentimentRequestDto(longInputString);

        SentimentResponseDto sentimentResponseDto = sentimentApiClient.callSentimentApi(sentimentRequestDto);

        log.info(sentimentResponseDto.toString());
    }
}
