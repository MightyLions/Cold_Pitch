package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.QSolution;
import com.ColdPitch.domain.entity.Solution;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class SolutionRepositoryImpl implements SolutionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public List<Solution> getAllByUserId(Long userId) {
        return queryFactory
            .selectFrom(QSolution.solution)
            .where(QSolution.solution.userId.eq(userId))
            .fetch();
    }
}
