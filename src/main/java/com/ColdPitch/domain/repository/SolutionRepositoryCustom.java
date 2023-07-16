package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Solution;
import java.util.List;

public interface SolutionRepositoryCustom {
    List<Solution> findAllByUserId(Long userId);
    Solution findByIdForUser(Long id);
    List<Solution> findAllForUser();
}
