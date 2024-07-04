package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.Match;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends BaseRepository<Match, UUID> {
    List<Match> findAllByTeam1Id(UUID teamId);
}
