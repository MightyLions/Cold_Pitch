package com.ColdPitch.domain.entity.dto.user;

import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationDto;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CompanyRequestDto {
    @NotNull
    UserRequestDto userRequestDto;
    @NotNull
    CompanyRegistrationDto companyRegistrationDto;
}
