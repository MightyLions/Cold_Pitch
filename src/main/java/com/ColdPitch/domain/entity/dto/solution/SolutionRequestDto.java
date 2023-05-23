package com.ColdPitch.domain.entity.dto.solution;

import com.ColdPitch.domain.entity.solution.SolutionState;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class SolutionRequestDto {
    private Long id;

    @NotNull
    private String feedback;

    @NotNull
    private String reaction;

    private SolutionState status;

    @NotNull
    @JsonProperty("positivePercentage")
    private String positivePercentage;

    @NotNull
    @JsonProperty("negativePercentage")
    private String negativePercentage;

    @JsonProperty("userId")
    @NotNull
    private Long userId;
}
