package com.ColdPitch.domain.entity.dto.companyRegistraion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationDto {

    @JsonProperty("b_no")
    @NotBlank
    private String b_no = "";

    @JsonProperty("start_dt")
    @NotBlank
    private String start_dt = "";

    @JsonProperty("p_nm")
    @NotBlank
    private String p_nm = "";

    @JsonProperty("b_nm")
    private String b_nm = "";

    @JsonProperty("corp_no")
    private String corp_no = "";

    @JsonProperty("b_sector")
    private String b_sector = "";

    @JsonProperty("b_type")
    private String b_type = "";

    @JsonProperty("b_adr")
    private String b_adr = "";

    @JsonProperty("company_description")
    private String companyDescription;

    @JsonProperty("valid")
    private String valid;

}