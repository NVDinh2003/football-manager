package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.MatchFilter;
import com.nvd.footballmanager.model.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends BaseRepository<Match, MatchFilter, UUID> {

    @Override
    @Query("""
            SELECT m FROM Match m
            WHERE ( :#{#filter.s == null || #filter.s.isEmpty()} = TRUE
                OR LOWER(m.venue) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
            )
            AND (:#{#filter.teamId == NULL} = TRUE OR m.team1.id = :#{#filter.teamId}) 
            AND (:#{#filter.fromDate == NULL} = TRUE OR m.time >= :#{#filter.fromDate})
            AND (:#{#filter.toDate == NULL} = TRUE OR m.time <= :#{#filter.toDate})
            """)
    Page<Match> findAllWithFilter(Pageable pageable, MatchFilter filter);

    @Query(value = "SELECT m.id FROM matches m LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<String> findRandomLimitOffset(int offset, int limit);

    @Query(value = "SELECT m.id FROM matches m", nativeQuery = true)
    List<String> findAllMatchIds();

}
