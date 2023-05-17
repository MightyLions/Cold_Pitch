package com.ColdPitch.domain.entity;

import com.ColdPitch.domain.entity.user.CurState;
import com.ColdPitch.domain.entity.user.UserType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class User extends BaseEntity {
    @Id //userId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userNickname;
    private boolean userSocial;// 확장성

    @Column(nullable = false)
    private String userPw;
    @Column(nullable = false)
    private String userEmail;
    @Column(nullable = false)
    private String userPhoneNumber;

    //companyRegisrationNumber

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private CurState curState;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", userSocial=" + userSocial +
                ", userPw='" + userPw + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", userType=" + userType +
                ", curState=" + curState +
                '}';
    }
}
