package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.QSolution;
import com.ColdPitch.domain.entity.Solution;
import com.ColdPitch.domain.entity.solution.SolutionState;
import com.ColdPitch.utils.SecurityUtil;
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
    public List<Solution> findByUserIdForUser(Long id) {
        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return queryFactory
                .selectFrom(QSolution.solution)
                .where(QSolution.solution.id.eq(id))
                .fetch();
        }

        return queryFactory
            .selectFrom(QSolution.solution)
            .where(QSolution.solution.id.eq(id))
            .where(QSolution.solution.status.ne(SolutionState.DELETE))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solution> findAllByUserId(Long userId) {
        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return queryFactory
                .selectFrom(QSolution.solution)
                .where(QSolution.solution.userId.eq(userId))
                .fetch();
        }

        return queryFactory
            .selectFrom(QSolution.solution)
            .where(QSolution.solution.userId.eq(userId))
            .where(QSolution.solution.status.ne(SolutionState.DELETE))
            .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public Solution findByIdForUser(Long id) {
        return queryFactory
            .selectFrom(QSolution.solution)
            .where(QSolution.solution.id.eq(id))
            .where(QSolution.solution.status.ne(SolutionState.DELETE))
            .fetch()
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solution> findAllForUser() {
        return queryFactory
            .selectFrom(QSolution.solution)
            .where(QSolution.solution.status.ne(SolutionState.DELETE))
            .fetch();
    }
}
