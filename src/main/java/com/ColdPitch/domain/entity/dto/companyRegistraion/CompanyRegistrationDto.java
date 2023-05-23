package com.ColdPitch.domain.entity.dto.companyRegistraion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationDto {

    @NotEmpty
    @Size(min = 10, max = 10)
    @JsonProperty("b_no")
    private String b_no = "";

    @NotEmpty
    @JsonProperty("start_dt")
    private String start_dt = "";

    @NotEmpty
    @JsonProperty("p_nm")
    private String p_nm = "";

    @NotEmpty
    @JsonProperty("b_nm")
    private String b_nm = "";

    @NotEmpty
    @Size(min = 13, max = 13)
    @JsonProperty("corp_no")
    private String corp_no = "";

    @NotEmpty
    @JsonProperty("b_sector")
    private String b_sector = "";

    @NotEmpty
    @JsonProperty("b_type")
    private String b_type = "";

    @NotEmpty
    @JsonProperty("company_address")
    private String companyAddress;

    @JsonProperty("valid")
    private String valid;

}