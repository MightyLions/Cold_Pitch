package com.ColdPitch.domain.entity.dto.user;

import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationDto;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CompanyRequestDto {
    UserRequestDto userRequestDto;
    CompanyRegistrationDto companyRegistrationDto;
}
