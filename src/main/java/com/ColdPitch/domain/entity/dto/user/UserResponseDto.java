package com.ColdPitch.domain.entity.dto.user;

import com.ColdPitch.domain.entity.user.CurState;
import com.ColdPitch.domain.entity.user.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResponseDto {
    //TODO 어디까지 반환할것 인지
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String phoneNumber;
    private UserType userType;
    private CurState curState;
}
