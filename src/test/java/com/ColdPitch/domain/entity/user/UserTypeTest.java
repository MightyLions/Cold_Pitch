package com.ColdPitch.domain.entity.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeTest {
    @Test
    public void test() {
        UserType business = UserType.of("BUSINESS");
        assertThat(business).isEqualTo(UserType.BUSINESS);
    }

}