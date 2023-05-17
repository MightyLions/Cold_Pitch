package com.ColdPitch.domain.entity.post;

import lombok.Getter;

@Getter
public enum PostState {
    CLOSED("CLOSED"), OPEN("OPEN"), PRIVATE("PRIVATE");

    private final String status;
    PostState(String status) {
        this.status = status;
    }
}
