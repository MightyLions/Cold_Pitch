package com.ColdPitch.domain.entity.dto.jwt;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequestDto {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
