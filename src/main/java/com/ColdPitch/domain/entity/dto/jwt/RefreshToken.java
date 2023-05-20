package com.ColdPitch.domain.entity.dto.jwt;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {
    @Id
    @Column(name = "refreshtoken_id")
    private String key;

    private String value;

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateValue(String token) {
        this.value = value;
        return this;
    }
}
