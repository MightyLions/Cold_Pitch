package com.ColdPitch.domain.entity.dto.companyRegistraion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationDto {

    @JsonProperty("b_no")
    private String b_no = "";

    @JsonProperty("start_dt")
    private String start_dt = "";

    @JsonProperty("p_nm")
    private String p_nm = "";

    @JsonProperty("b_nm")
    private String b_nm = "";

    @JsonProperty("corp_no")
    private String corp_no = "";

    @JsonProperty("b_sector")
    private String b_sector = "";

    @JsonProperty("b_type")
    private String b_type = "";

    @JsonProperty("company_address")
    private String companyAddress;

    @JsonProperty("valid")
    private String valid;

}