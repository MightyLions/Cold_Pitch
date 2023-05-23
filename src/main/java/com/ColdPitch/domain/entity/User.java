package com.ColdPitch.domain.entity;

import com.ColdPitch.domain.entity.dto.user.UserRequestDto;
import com.ColdPitch.domain.entity.user.CurState;
import com.ColdPitch.domain.entity.user.UserType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@SQLDelete(sql = "UPDATE user SET cur_state = 'DELETED' WHERE user_id = ?")
@Where(clause = "cur_state != 'DELETED'")
public class User extends BaseEntity {
    @Id //userId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    private boolean userSocial;// 확장성

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToOne(mappedBy = "user")
    private CompanyRegistration companyRegistration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

    private CurState curState;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userSocial=" + userSocial +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userType=" + userType +
                ", curState=" + curState +
                super.toString() +
                '}';
    }

    //유저 수정 부분
    public void updateProfile(UserRequestDto userRequestDto) {
        this.name = userRequestDto.getName();
        this.nickname = userRequestDto.getNickname();
        this.phoneNumber = userRequestDto.getPhoneNumber();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void registerCompany(CompanyRegistration companyRegistration) {
        this.companyRegistration = companyRegistration;
    }
}
