package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.MatchFilter;
import com.nvd.footballmanager.model.entity.Match;
import com.nvd.footballmanager.model.view_statistical.TeamMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
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

    @Query("""
            SELECT m FROM Match m 
            WHERE m.confirmed = false AND m.updatedAt < :now
            """)
    List<Match> findAllByTimeBeforeAndNotConfirmed(@Param("now") Instant now);

    @Query(value = "SELECT m.* FROM matches m " +
            "LEFT JOIN member_match mm ON m.id = mm.match_id " +
            "WHERE mm.member_id = :#{#filter.memberId} " +
            "AND m.time BETWEEN :#{#filter.fromDate} AND :#{#filter.toDate}",
            nativeQuery = true)
    List<Match> findAllByPlayerAndTimeRange(@Param("filter") MatchFilter filter);


    @Query("""
            SELECT DATE(m.time) AS date, COUNT(m.id) AS match_count FROM Match m
            JOIN m.team1 t1 on m.team1.id = t1.id
            JOIN m.team2 t2 on m.team2.id = t2.id
            WHERE (t1.id = :#{#filter.teamId} OR t2.id = :#{#filter.teamId})
            AND m.time BETWEEN :#{#filter.fromDate} AND :#{#filter.toDate}
            GROUP BY DATE(m.time)
            """)
    List<TeamMatch> findAllByTeamAndTimeRange(MatchFilter filter);
}
