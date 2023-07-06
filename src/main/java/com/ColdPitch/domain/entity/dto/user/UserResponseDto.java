package com.ColdPitch.domain.entity.dto.user;

import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.user.CurState;
import com.ColdPitch.domain.entity.user.UserType;
import lombok.*;

@Getter
@Setter
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

    public UserResponseDto(User savedMember) {
        this.id = savedMember.getId();
        this.name = savedMember.getName();
        this.nickname = savedMember.getNickname();
        this.email = savedMember.getEmail();
        this.phoneNumber = savedMember.getPhoneNumber();
        this.userType = savedMember.getUserType();
        this.curState = savedMember.getCurState();
    }

    public static UserResponseDto of(User savedMember) {
        return UserResponseDto.builder().id(savedMember.getId())
                .name(savedMember.getName())
                .nickname(savedMember.getNickname())
                .email(savedMember.getEmail())
                .phoneNumber(savedMember.getPhoneNumber())
                .userType(savedMember.getUserType())
                .curState(savedMember.getCurState())
                .build();
    }

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userType=" + userType +
                ", curState=" + curState +
                '}';
    }
}
