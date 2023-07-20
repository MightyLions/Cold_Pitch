package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.File;
import com.ColdPitch.domain.entity.FileStatus;
import com.ColdPitch.domain.entity.dto.file.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
@Disabled
class FileRepositoryTest {
    @Autowired
    EntityManager entityManager;
    @Autowired
    private FileQueryRepository fileQueryRepository;
    @Autowired
    private FileJPARepository<File> fileFileJPARepository;

    @BeforeEach
    public void initData() {
        for (int i = 0; i < 10; i++) {
            FileStatus status = FileStatus.C;
            if (i % 3 == 0) {
                status = FileStatus.D;
            }

            File newFile = File.builder().name(UUID.randomUUID().toString())
                    .size((long) new Random().nextInt(9000000))
                    .status(status)
                    .build();

            File save = fileFileJPARepository.save(newFile);
            log.info(save.toString());
        }
    }

    @Test
    void findAll() {
        List<FileUploadResponse> files = fileQueryRepository.findAll();

        assertThat(files.size()).isEqualTo(9);
    }
//
//    @Test
//    void findByPath() {
//    }
}