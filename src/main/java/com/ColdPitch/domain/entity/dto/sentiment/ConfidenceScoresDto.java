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
public class ConfidenceScoresDto {
    private double neutral;
    private double positive;
    private double negative;

    @Override
    public String toString() {
        return "ConfidenceScoresDto{" +
                "neutral=" + neutral +
                ", positive=" + positive +
                ", negative=" + negative +
                '}';
    }
}
