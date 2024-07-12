package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.AchievementFilter;
import com.nvd.footballmanager.model.entity.Achievement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AchievementRepository extends BaseRepository<Achievement, AchievementFilter, UUID> {
    List<Achievement> findAllByTeamId(UUID teamId);

    @Override
    @Query("""
            SELECT a FROM Achievement a 
            WHERE ( :#{#filter.s == null || #filter.s.isEmpty()} = TRUE
                OR LOWER(a.title) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
            )
            AND (:#{#filter.teamId == NULL} = TRUE OR a.team.id = :#{#filter.teamId}) 
            """)
    Page<Achievement> findAllWithFilter(Pageable pageable, AchievementFilter filter);
}
