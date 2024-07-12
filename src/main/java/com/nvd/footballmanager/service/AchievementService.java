package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.AchievementDTO;
import com.nvd.footballmanager.filters.AchievementFilter;
import com.nvd.footballmanager.mappers.AchievementMapper;
import com.nvd.footballmanager.model.entity.Achievement;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.repository.AchievementRepository;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
//@RequiredArgsConstructor
public class AchievementService extends BaseService<Achievement, AchievementDTO, AchievementFilter, UUID> {

    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    private final TeamRepository teamRepository;

    public AchievementService(AchievementRepository achievementRepository,
                              AchievementMapper achievementMapper,
                              TeamRepository teamRepository) {
        super(achievementRepository, achievementMapper);
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
        this.teamRepository = teamRepository;
    }

    public AchievementDTO addAchievement(UUID teamId, AchievementDTO achievementDTO) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Achievement achievement = achievementMapper.convertToEntity(achievementDTO);
        achievement.setTeam(team);

        Achievement savedAch = achievementRepository.save(achievement);

        return achievementMapper.convertToDTO(savedAch);
    }

    public Page<AchievementDTO> getAllAchievements(AchievementFilter filter) {
        teamRepository.findById(filter.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        return super.findAll(filter);
    }

}
