/**
 * @project ColdPitch
 * @author ARA
 * @since 2023-07-19 AM 4:52
 */

package com.ColdPitch.domain.entity.dto.sentiment;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SentimentClassifiedResponseDto {
    private List<Map.Entry<String, Integer>> positiveWordMapList;
    private List<Map.Entry<String, Integer>> negativeWordMapList;
}
