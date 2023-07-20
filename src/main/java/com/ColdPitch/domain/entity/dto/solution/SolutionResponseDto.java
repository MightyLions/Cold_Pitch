package com.ColdPitch.domain.entity.dto.solution;

import com.ColdPitch.domain.entity.solution.SolutionState;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SolutionResponseDto {
    private Long id;

    @NotNull
    private String feedback;

    @NotNull
    private String reaction;

    @NotNull
    @JsonProperty("positivePercentage")
    private String positivePercentage;

    @NotNull
    @JsonProperty("negativePercentage")
    private String negativePercentage;

    @JsonProperty("userId")
    @NotNull
    private Long userId;

    @NotNull
    private SolutionState status;

    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private String createBy;
    private String modifiedBy;
}
