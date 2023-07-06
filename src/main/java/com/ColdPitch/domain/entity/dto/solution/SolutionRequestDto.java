package com.ColdPitch.domain.entity.dto.solution;

import com.ColdPitch.domain.entity.solution.SolutionState;
import com.ColdPitch.validation.annotations.ValidEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class SolutionRequestDto {
    private Long id;

    @NotNull
    @NotBlank
    private String feedback;

    @NotNull
    @NotBlank
    private String reaction;

    @ValidEnum(enumClass = SolutionState.class)
    private SolutionState status;

    @JsonProperty("positivePercentage")
    @NotNull
    @NotBlank
    private String positivePercentage;

    @JsonProperty("negativePercentage")
    @NotNull
    @NotBlank
    private String negativePercentage;

    @JsonProperty("userId")
    @NotNull
    @Size
    private Long userId;
}
