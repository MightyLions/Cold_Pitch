package com.ColdPitch.domain.entity.user;

public enum UserType {
    BUSINESS, USER, ADMIN;

    public static UserType of(String userType) {
        return UserType.valueOf(userType);
    }
}
