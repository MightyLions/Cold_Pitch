package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.File;
import com.ColdPitch.domain.entity.dto.file.FileUploadResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileJPARepository<T> extends JpaRepository<File, Long>, QuerydslPredicateExecutor<T>, FileRepositoryCustom{

}
