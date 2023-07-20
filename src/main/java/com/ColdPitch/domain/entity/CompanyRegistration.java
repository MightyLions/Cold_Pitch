package com.ColdPitch.domain.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class CompanyRegistration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    //사업자 등록번호 "0123456789" 10자 (필수)
    @Column(name = "company_registration_number", nullable = false)
    private String b_no;

    //상호 "(주)테스트"
    @Column(name = "company_name", nullable = false)
    private String b_nm;

    //대표자 이름 "홍길동" (필수)
    @Column(name = "corporate_representative", nullable = false)
    private String p_nm;

    //개업일자 "20230101" YYYYMMDD (필수)
    @Column(name = "commencement_date", nullable = false)
    private String start_dt;

    //법인등록번호 "0123456789123" 13자
    @Column(name = "corporate_registration_number", nullable = false)
    private String corp_no;

    //주업태명 "부동산업"
    @Column(name = "company_sector", nullable = false)
    private String b_sector;

    //주종목명 "부동산중개업"
    @Column(name = "company_type", nullable = false)
    private String b_type;

    //사업장 주소
    @Column(name = "company_address", nullable = false)
    private String b_adr;

    //기업 소개
    @Column(name = "company_description", nullable = false)
    private String companyDescription;
  
    @OneToOne(fetch = FetchType.LAZY
            , cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id"
            , foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Override
    public String toString() {
        return "CompanyRegistration {" +
                "id=" + id +
                ", b_no='" + b_no + '\'' +
                ", b_nm='" + b_nm + '\'' +
                ", p_nm='" + p_nm + '\'' +
                ", start_dt='" + start_dt + '\'' +
                ", corp_no='" + corp_no + '\'' +
                ", b_sector='" + b_sector + '\'' +
                ", b_type='" + b_type + '\'' +
                ", b_adr='" + b_adr + '\'' +
                ", " + super.toString() +
                '}';
    }
}