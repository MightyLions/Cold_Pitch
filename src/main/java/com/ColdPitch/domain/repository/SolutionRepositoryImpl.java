package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.QSolution;
import com.ColdPitch.domain.entity.Solution;
import com.ColdPitch.domain.entity.solution.SolutionState;
import com.ColdPitch.domain.repository.support.Querydsl4RepositorySupport;
import com.ColdPitch.utils.SecurityUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.ColdPitch.domain.entity.QSolution.*;

@Transactional(readOnly = true)
@Repository
public class SolutionRepositoryImpl extends Querydsl4RepositorySupport implements SolutionRepositoryCustom {
    public SolutionRepositoryImpl() {
        super(Solution.class);
    }

    @Override
    @Transactional
    public List<Solution> findAllByUserId(Long userId) {
        return selectFrom(solution)
                .where(solution.userId.eq(userId))
                .where(enableDelete())
                .fetch();
    }

    @Override
    @Transactional
    public Solution findByIdForUser(Long id) {
        return selectFrom(solution)
                .where(solution.id.eq(id))
                .where(enableDelete())
                .fetchOne();
    }

    @Override
    @Transactional
    public List<Solution> findAllForUser() {
        return selectFrom(solution)
            .where(solution.status.ne(SolutionState.DELETE))
            .fetch();
    }

    private static BooleanExpression enableDelete() {
        return isAdmin() ? null : solution.status.ne(SolutionState.DELETE) ;
    }

    private static boolean isAdmin() {
        return SecurityUtil.checkCurrentUserRole("ADMIN");
    }
}
