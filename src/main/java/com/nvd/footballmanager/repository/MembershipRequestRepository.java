package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.BaseFilter;
import com.nvd.footballmanager.model.entity.MembershipRequest;
import com.nvd.footballmanager.model.enums.MembershipRequestStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipRequestRepository extends BaseRepository<MembershipRequest, BaseFilter, UUID> {

    Optional<MembershipRequest> findByUserIdAndTeamId(UUID userId, UUID teamId);

    List<MembershipRequest> findAllByUserId(UUID userId);

    List<MembershipRequest> findAllByTeamId(UUID teamId);

    @Query("""
            SELECT mr FROM MembershipRequest mr
            WHERE mr.status = :status
            AND mr.requestDate < :thresholdTime
            """)
    List<MembershipRequest> findByStatusAndTime(@Param("status") MembershipRequestStatus status,
                                                @Param("thresholdTime") Instant thresholdTime);

}
