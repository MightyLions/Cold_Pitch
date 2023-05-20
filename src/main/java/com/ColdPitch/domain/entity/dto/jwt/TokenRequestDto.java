package com.ColdPitch.domain.entity.dto.jwt;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
