package com.ColdPitch.domain.entity.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TagName {
    LOGISTICS("Logistics"),
    IT("IT"),
    SERVICE("Service"),
    CONSTRUCTION("Construction"),
    FINANCE("Finance"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    MARKETING("Marketing"),
    FASHION("Fashion"),
    TRAVEL("Travel"),
    FOOD("Food"),
    SPORTS("Sports");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }
}
