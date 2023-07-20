package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.CompanyRegistration;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationDto;
import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationValidationDto;
import com.ColdPitch.domain.repository.CompanyRegistrationRepository;
import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

    //연관관계 매핑
    public CompanyRegistration validateAndSaveCompanyRegistration(CompanyRegistrationDto companyRegistrationDto, User user) {

        // 유효성 검사
        CompanyRegistrationValidationDto validationDto = new CompanyRegistrationValidationDto(
                companyRegistrationDto.getB_no(),
                companyRegistrationDto.getStart_dt(),
                companyRegistrationDto.getP_nm()
        );

        if (companyRegistrationValidator.validateCompanyRegistration(validationDto)) {
            CompanyRegistration companyRegistration = convertDtoToEntity(companyRegistrationDto, user);
            return companyRegistrationRepository.save(companyRegistration);
        }
        throw new CustomException(ErrorCode.COMPANY_NOT_FOUND);
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
                .b_adr(dto.getB_adr())
                .companyDescription(dto.getCompanyDescription())
                .build();
    }

    private CompanyRegistration convertDtoToEntity(CompanyRegistrationDto dto, User user) {
        return CompanyRegistration.builder()
                .b_no(dto.getB_no())
                .start_dt(dto.getStart_dt())
                .p_nm(dto.getP_nm())
                .b_nm(dto.getB_nm())
                .corp_no(dto.getCorp_no())
                .b_sector(dto.getB_sector())
                .b_type(dto.getB_type())
                .b_adr(dto.getB_adr())
                .companyDescription(dto.getCompanyDescription())
                .user(user)
                .build();
    }
}