package com.ColdPitch.domain.repository;

import static com.ColdPitch.domain.utils.RandomUtil.getRandom;
import static com.ColdPitch.domain.utils.RandomUtil.getRandomPercentage;

import com.ColdPitch.domain.entity.Solution;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
public class SolutionRepositoryTest {

    @Autowired
    private SolutionRepository solutionRepository;
    List<Solution> solutionList;
    Long SELECTED_ID = 1L;
    Long USER_ID = 1L;
    Long UPDATED_UD = 2L;
    int REPEATED_COUNT = 10;

    @BeforeAll
    void initTask() {
        solutionList = new ArrayList<>();

        for (int i = 0; i < REPEATED_COUNT; i++) {
            double randomVal = 1 - getRandomPercentage();

            Solution solution = Solution.builder()
                .negativePercentage(String.format("%.9f", randomVal).substring(0, 10))
                .positivePercentage(String.format("%.9f", 1 - randomVal).substring(0, 10))
                .feedback("feedback " + i)
                .reaction("reaction " + i)
                .userId(getRandom(REPEATED_COUNT))
                .build();

            if (i == 0) {
                solution = solution.toBuilder().id(1L).build();
            }

            solution = solutionRepository.saveAndFlush(solution);
            solutionList.add(solution);
        }
    }

    @AfterAll
    void destructor() {
        solutionRepository.deleteAll(solutionList);
    }

    @Test
    void selectTest() {
        Solution sol = solutionRepository.findById(1L).orElse(null);

        log.info(sol.toString());
    }

    @Test
    void insertTest() {
        double randomVal = 1 - getRandomPercentage();

        Solution sol = Solution.builder()
            .negativePercentage(String.format("%.9f", randomVal).substring(0, 10))
            .positivePercentage(String.format("%.9f", 1 - randomVal).substring(0, 10))
            .reaction("test reaction")
            .feedback("feedback")
            .userId(USER_ID)
            .build();

        sol = solutionRepository.saveAndFlush(sol);
        solutionList.add(sol);

        log.info(sol.toString());
    }

    @Test
    void updateTest() {
        Solution sol = solutionRepository.findById(1L).orElse(null);

        sol = sol.toBuilder()
            .reaction("updated reaction")
            .feedback("updated feedback")
            .build();

        sol = solutionRepository.saveAndFlush(sol);

        log.info(sol.toString());
    }

    @Test
    void deleteTest() {
        solutionRepository.deleteById(1L);
        List<Solution> list = solutionRepository.findAll();

        list.forEach(solution -> {log.info(solution.toString());});
    }
}
