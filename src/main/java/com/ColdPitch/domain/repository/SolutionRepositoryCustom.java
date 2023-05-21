package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Solution;
import java.util.List;

public interface SolutionRepositoryCustom {
    List<Solution> getAllByUserId(Long userId);
}
