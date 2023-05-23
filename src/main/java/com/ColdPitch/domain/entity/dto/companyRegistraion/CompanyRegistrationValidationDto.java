package com.ColdPitch.domain.entity.dto.companyRegistraion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationValidationDto {
    private String b_no;
    private String start_dt;
    private String p_nm;
}