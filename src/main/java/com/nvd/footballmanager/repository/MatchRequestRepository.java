package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.MatchRequestFilter;
import com.nvd.footballmanager.model.entity.MatchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRequestRepository extends BaseRepository<MatchRequest, MatchRequestFilter, UUID> {

    @Override
    @Query("""
            SELECT m FROM MatchRequest m 
            WHERE ( :#{#filter.s == null || #filter.s.isEmpty()} = TRUE
                OR LOWER(m.venue) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
            )
            AND (:#{#filter.teamId == NULL} = TRUE OR m.team.id = :#{#filter.teamId}) 
            AND (:#{#filter.status == null} = TRUE OR m.status = :#{#filter.status})
            AND (:#{#filter.fromDate == null} = TRUE OR m.time >= :#{#filter.fromDate})
            AND (:#{#filter.toDate == null} = TRUE OR m.time <= :#{#filter.toDate})
            AND (:#{#filter.matchType == null} = TRUE OR m.matchType = :#{#filter.matchType})
            """)
    Page<MatchRequest> findAllWithFilter(Pageable pageable, MatchRequestFilter filter);

    @Query("""
            SELECT m FROM MatchRequest m 
            WHERE m.team.id != :#{#filter.teamId}
            OR LOWER(m.locationDetails) LIKE LOWER(CONCAT('%', :#{#filter.locationDetails}, '%'))
            AND (:#{#filter.status == null} = TRUE OR m.status = :#{#filter.status})               
            OR (:#{#filter.rankPoints == null} = TRUE OR m.team.rankPoints >= :#{#filter.rankPoints} - :#{#filter.rankPointRange})
            OR (:#{#filter.rankPoints == null} = TRUE OR m.team.rankPoints <= :#{#filter.rankPoints} + :#{#filter.rankPointRange})
            """)
    Page<MatchRequest> suggest(Pageable pageable, MatchRequestFilter filter);

    @Query(value = "SELECT * FROM match_requests ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<MatchRequest> findRandomLimit(int limit);
}
