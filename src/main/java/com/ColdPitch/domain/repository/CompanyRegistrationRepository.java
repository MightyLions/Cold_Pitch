package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.CompanyRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRegistrationRepository extends JpaRepository<CompanyRegistration, Long> {
}