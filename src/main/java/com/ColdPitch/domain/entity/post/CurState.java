package com.ColdPitch.domain.entity.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum CurState {
    CLOSED("CLOSED"), OPEN("OPEN"), PRIVATE("PRIVATE");

    private final String status;
    CurState(String status) {
        this.status = status;
    }
}
