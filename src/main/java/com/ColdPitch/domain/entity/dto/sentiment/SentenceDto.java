/**
 * @project ColdPitch
 * @author ARA
 * @since 2023-07-15 AM 9:51
 */

package com.ColdPitch.domain.entity.dto.sentiment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SentenceDto {
    private String content;
    private String sentiment;
    private ConfidenceScoresDto confidence;

    @Override
    public String toString() {
        return "SentenceDto{" +
                "content='" + content + '\'' +
                ", sentiment='" + sentiment + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
