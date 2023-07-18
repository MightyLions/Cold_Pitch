/**
 * @project ColdPitch
 * @author ARA
 */

package com.ColdPitch.domain.component;

import com.ColdPitch.domain.service.IWordAnalysisService;
import com.ColdPitch.domain.service.WordAnalysisService;
import com.ColdPitch.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

@Slf4j
public class WordAnalysisServiceTest {
    private final IWordAnalysisService wordAnalysisService = new WordAnalysisService();

    private final String shortInputString = "싸늘하다. 가슴에 비수가 날아와 꽂힌다.";
    private final String longInputString = "없는 가슴에 품으며, 아름답고 얼음이 봄바람을 찾아다녀도, " +
            "청춘의 살 것이다. 밥을 위하여, 창공에 뿐이다. 이상 눈에 내는 것이다. 인간의 그러므로 동산에는 " +
            "풀이 노년에게서 작고 끓는 열매를 고행을 사막이다. " +
            "인간에 하여도 것은 트고, 들어 인생에 청춘에서만 소리다.이것은 싸인 운다. " +
            "있는 어디 얼마나 사라지지 거친 인류의 꾸며 아니다. 쓸쓸한 창공에 할지라도 것이다." +
            "보라, 붙잡아 열락의 피에 아름답고 없으면 봄바람이다. 공자는 이상 보이는 운다. " +
            "뜨고, 못하다 긴지라 그와 말이다. 그러므로 생생하며, 창공에 불러 힘차게 품고 피다.";


    @Test
    public void testNouns() throws Exception {
        List<String> list = wordAnalysisService.doWordNouns(shortInputString);
        List<String> list2 = wordAnalysisService.doWordNouns(longInputString);

        log.info(list.toString());
        log.info(list2.toString());
    }

    @Test
    public void testWordAnalysis() throws Exception {
        Map<String, Integer> map = wordAnalysisService.doWordAnalysis(shortInputString);
        Map<String, Integer> map2 = wordAnalysisService.doWordAnalysis(longInputString);

        log.info(shortInputString);
        map.forEach((s, integer) -> {
            log.info("{} : {}", s, integer);
        });

        log.info(longInputString);
        map2.forEach((s, integer) -> {
            log.info("{} : {}", s, integer);
        });
    }

    @Test
    public void testWordCount() throws Exception {
        Map<String, Integer> map = wordAnalysisService
                .doWordCount(wordAnalysisService.doWordNouns(shortInputString));
        Map<String, Integer> map2 = wordAnalysisService
                .doWordCount(wordAnalysisService.doWordNouns(longInputString));

        log.info(map.toString());
        log.info(map2.toString());
    }

    @Test
    public void wordAnalysisExceptionHandlingTest() {
        log.warn(
                Assertions.assertThatThrownBy(() -> {
                    wordAnalysisService.doWordNouns(null);
                })
                .isInstanceOf(CustomException.class)
                .toString()
        );
    }
}
