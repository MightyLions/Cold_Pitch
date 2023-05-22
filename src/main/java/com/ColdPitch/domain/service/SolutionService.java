package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Solution;
import com.ColdPitch.domain.entity.dto.solution.SolutionRequestDto;
import com.ColdPitch.domain.entity.dto.solution.SolutionResponseDto;
import com.ColdPitch.domain.repository.SolutionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SolutionService {
    private final SolutionRepository solutionRepository;

    public SolutionResponseDto saveSolutionRequestDto(SolutionRequestDto dto) {
        Solution solution = Solution.builder()
            .userId(dto.getUserId())
            .feedback(dto.getFeedback())
            .reaction(dto.getReaction())
            .positivePercentage(dto.getPositivePercentage())
            .negativePercentage(dto.getNegativePercentage())
            .build();

        solution = solutionRepository.saveAndFlush(solution);

        SolutionResponseDto responseDto = solutionToSolutionResponseDto(solution);

        return responseDto;
    }

    public static SolutionResponseDto solutionToSolutionResponseDto(Solution solution) {
        if (solution == null) {
            return null;
        }

        SolutionResponseDto responseDto = SolutionResponseDto.builder()
            .id(solution.getId())
            .userId(solution.getUserId())
            .feedback(solution.getFeedback())
            .reaction(solution.getReaction())
            .positivePercentage(solution.getPositivePercentage())
            .negativePercentage(solution.getNegativePercentage())
            .createAt(solution.getCreateAt())
            .createBy(solution.getCreatedBy())
            .modifiedAt(solution.getModifiedAt())
            .modifiedBy(solution.getModifiedBy())
            .build();

        return responseDto;
    }

    public SolutionResponseDto updateSolution(SolutionRequestDto dto) {
        Solution solution = solutionRepository.findById(dto.getId()).orElse(null);

        if (solution == null) {
            return null;
        }

        solution = solution.toBuilder()
            .reaction(dto.getReaction())
            .feedback(dto.getFeedback())
            .positivePercentage(dto.getPositivePercentage())
            .negativePercentage(dto.getNegativePercentage())
            .build();

        solution = solutionRepository.saveAndFlush(solution);

        return solutionToSolutionResponseDto(solution);
    }
}
