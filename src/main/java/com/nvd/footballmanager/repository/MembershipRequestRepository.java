package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.MembershipRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipRequestRepository extends BaseRepository<MembershipRequest, UUID> {
    Optional<MembershipRequest> findByUserIdAndTeamId(UUID userId, UUID teamId);

    List<MembershipRequest> findAllByUserId(UUID userId);

    List<MembershipRequest> findAllByTeamId(UUID teamId);
}
