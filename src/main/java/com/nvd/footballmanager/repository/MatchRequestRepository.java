package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.MatchRequest;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatchRequestRepository extends BaseRepository<MatchRequest, UUID> {
}
