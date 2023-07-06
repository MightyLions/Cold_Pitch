package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationDto;
import com.ColdPitch.domain.service.CompanyRegistrationService;
import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company")
public class CompanyRegistrationController {
    private final CompanyRegistrationService companyRegistrationService;

    @PostMapping("/validate")
    public ResponseEntity<CompanyRegistrationDto> validate(
            @Valid @RequestBody CompanyRegistrationDto companyRegistrationDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new CustomException(ErrorCode.POST_BAD_REQUEST);
        }

        boolean isValid = companyRegistrationService.validateAndSaveCompanyRegistration(companyRegistrationDto);

        if (!isValid) {
            throw new CustomException(ErrorCode.COMPANY_NOT_FOUND);
        }

        return ResponseEntity.ok(companyRegistrationDto);
    }
}