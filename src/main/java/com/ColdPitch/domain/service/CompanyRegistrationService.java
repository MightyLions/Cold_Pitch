package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.CompanyRegistration;
import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationDto;
import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationValidationDto;
import com.ColdPitch.domain.repository.CompanyRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyRegistrationService {
    private final CompanyRegistrationValidator companyRegistrationValidator;
    private final CompanyRegistrationRepository companyRegistrationRepository;

    public boolean validateAndSaveCompanyRegistration(CompanyRegistrationDto companyRegistrationDto) {
        // 유효성 검사
        CompanyRegistrationValidationDto validationDto = new CompanyRegistrationValidationDto(
                companyRegistrationDto.getB_no(),
                companyRegistrationDto.getStart_dt(),
                companyRegistrationDto.getP_nm()
        );
        boolean isValid = companyRegistrationValidator.validateCompanyRegistration(validationDto);

        if (isValid) {
            CompanyRegistration companyRegistration = convertDtoToEntity(companyRegistrationDto);
            companyRegistrationRepository.save(companyRegistration);
        }
        return isValid;
    }

    private CompanyRegistration convertDtoToEntity(CompanyRegistrationDto dto) {
        return CompanyRegistration.builder()
                .b_no(dto.getB_no())
                .start_dt(dto.getStart_dt())
                .p_nm(dto.getP_nm())
                .b_nm(dto.getB_nm())
                .corp_no(dto.getCorp_no())
                .b_sector(dto.getB_sector())
                .b_type(dto.getB_type())
                .companyAddress(dto.getCompanyAddress())
                .companyDescription(dto.getCompanyDescription())
                .build();
    }
}
