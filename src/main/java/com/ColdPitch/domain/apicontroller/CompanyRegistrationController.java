package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationDto;
import com.ColdPitch.domain.service.CompanyRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CompanyRegistrationController {
    private final CompanyRegistrationService companyRegistrationService;

    @PostMapping("/validate")
    public ResponseEntity<CompanyRegistrationDto> validate(@Valid @RequestBody CompanyRegistrationDto companyRegistrationDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        boolean isValid = companyRegistrationService.validateAndSaveCompanyRegistration(companyRegistrationDto);

        if (!isValid) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(companyRegistrationDto);
    }
}