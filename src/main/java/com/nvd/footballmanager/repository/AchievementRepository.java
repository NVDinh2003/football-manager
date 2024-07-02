package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.Achievement;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AchievementRepository extends BaseRepository<Achievement, UUID> {
    List<Achievement> findAllByTeamId(UUID teamId);
}
