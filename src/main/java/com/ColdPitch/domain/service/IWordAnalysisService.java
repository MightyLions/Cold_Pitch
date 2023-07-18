/**
 * @project ColdPitch
 * @author ARA
 */

package com.ColdPitch.domain.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IWordAnalysisService {
    //자연어 처리 - 형태소 분석 (명사만 추출하기)
    List<String> doWordNouns(String text);

    //빈도수 분석(단어별 출현 빈도수)
    Map<String, Integer> doWordCount(List<String> pList);

    //분석할 문장의 자연어 처리 및 빈도수 분석 수행
    Map<String, Integer> doWordAnalysis(String text);
}
