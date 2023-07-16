package com.ColdPitch.domain.entity.dto.user;

import com.ColdPitch.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyResponseDto extends UserResponseDto {
    private String b_no;
    private String start_dt;
    private String p_nm;
    private String b_nm;
    private String b_type;
    private String b_adr;
    private String description;

    public CompanyResponseDto(User savedMember) {
        super(savedMember);
        this.b_no = savedMember.getCompanyRegistration().getB_no();
        this.start_dt = savedMember.getCompanyRegistration().getStart_dt();
        this.p_nm = savedMember.getCompanyRegistration().getP_nm();
        this.b_nm = savedMember.getCompanyRegistration().getB_nm();
        this.b_type = savedMember.getCompanyRegistration().getB_type();
        this.b_adr = savedMember.getCompanyRegistration().getB_adr();
        this.description = savedMember.getCompanyRegistration().getCompanyDescription();
    }

    @Override
    public String toString() {
        return "CompanyResponseDto{" +
                super.toString() +
                "b_no='" + b_no + '\'' +
                ", start_dt='" + start_dt + '\'' +
                ", p_nm='" + p_nm + '\'' +
                ", b_nm='" + b_nm + '\'' +
                ", b_type='" + b_type + '\'' +
                ", address='" + b_adr + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
