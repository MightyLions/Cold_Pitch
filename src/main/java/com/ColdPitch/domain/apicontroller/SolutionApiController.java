package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.Solution;
import com.ColdPitch.domain.entity.dto.solution.SolutionRequestDto;
import com.ColdPitch.domain.entity.dto.solution.SolutionResponseDto;
import com.ColdPitch.domain.repository.SolutionRepository;
import com.ColdPitch.domain.service.SolutionService;
import com.ColdPitch.utils.RandomUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SolutionApiController {

    private final SolutionRepository solutionRepository;
    private final SolutionService solutionService;

    @GetMapping("/solution")
    @Transactional(readOnly = true)
    public ResponseEntity<List<SolutionResponseDto>> getSolution(Long solutionId) {
        if (solutionId == null) {
            return ResponseEntity.ok(
                solutionRepository
                    .findAll()
                    .stream()
                    .map(SolutionService::solutionToSolutionResponseDto)
                    .collect(Collectors.toList()));
        }

        if (!solutionRepository.existsById(solutionId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(solutionRepository
            .findById(solutionId)
            .stream()
            .map(SolutionService::solutionToSolutionResponseDto)
            .collect(Collectors.toList()));
    }

    @PostMapping("/solution")
    public ResponseEntity<SolutionResponseDto> postSolution(SolutionRequestDto dto) {
        return ResponseEntity.ok(solutionService.saveSolutionRequestDto(dto));
    }

    @PatchMapping("/solution")
    public ResponseEntity<SolutionResponseDto> patchSolution(SolutionRequestDto requestDto) {
        SolutionResponseDto responseDto = solutionService.updateSolution(requestDto);

        if (responseDto == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/solution")
    public ResponseEntity<SolutionResponseDto> deleteSolution(Long solutionId) {
        if (solutionRepository.findById(solutionId).isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        solutionRepository.deleteById(solutionId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/solution/dummy")
    public ResponseEntity<List<SolutionResponseDto>> postSolution(Integer count) {
        List<SolutionResponseDto> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Solution solution = Solution.builder()
                .userId(RandomUtil.getRandom(count))
                .reaction("dummy reaction " + i)
                .feedback("dummy feedback " + i)
                .positivePercentage(String.format("%.9f", RandomUtil.getRandomPercentage()).substring(0, 10))
                .negativePercentage(String.format("%.9f", 1 - RandomUtil.getRandomPercentage()).substring(0, 10))
                .build();

            if (i % 2 == 0) {
                solution = solution.toBuilder()
                    .userId(2L)
                    .build();
            }

            solution = solutionRepository.saveAndFlush(solution);
            list.add(solutionService.solutionToSolutionResponseDto(solution));
        }

        return ResponseEntity.ok(list);
    }
}
