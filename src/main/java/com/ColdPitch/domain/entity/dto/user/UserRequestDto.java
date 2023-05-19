package com.ColdPitch.domain.entity.dto.user;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserRequestDto {
    @NotNull
    private String nickname;
    @NotNull
    private String name;
    @NotNull
    private String password;
    @NotNull
    private String email;

    private String phoneNumber;
    @NotNull
    private String userType;

}
