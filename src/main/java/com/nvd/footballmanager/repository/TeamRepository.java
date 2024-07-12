package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.BaseFilter;
import com.nvd.footballmanager.model.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamRepository extends BaseRepository<Team, BaseFilter, UUID> {
    @Override
    @Query("""
            SELECT t FROM Team t
            WHERE ( :#{#filter.s == null || #filter.s.isEmpty()} = TRUE
                OR LOWER(t.name) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
                OR LOWER(t.address) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
            )
            """)
    Page<Team> findAllWithFilter(Pageable pageable, BaseFilter filter);
}
