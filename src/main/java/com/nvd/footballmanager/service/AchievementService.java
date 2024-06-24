package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.AchievementDTO;
import com.nvd.footballmanager.mappers.AchievementMapper;
import com.nvd.footballmanager.model.entity.Achievement;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.repository.AchievementRepository;
import com.nvd.footballmanager.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
//@RequiredArgsConstructor
public class AchievementService extends BaseService<Achievement, AchievementDTO, UUID> {

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
                .orElseThrow(EntityNotFoundException::new);

        Achievement achievement = achievementMapper.convertToEntity(achievementDTO);
        achievement.setTeam(team);

        Achievement savedAch = achievementRepository.save(achievement);

        return achievementMapper.convertToDTO(savedAch);
    }

    public List<AchievementDTO> getAllAchievements(UUID teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(EntityNotFoundException::new);

        List<Achievement> list = achievementRepository.findAllByTeamId(teamId);
        return achievementMapper.convertListToDTO(list);
    }

//    @Transactional
//    public AchievementDTO updateAchievement(UUID teamId, UUID achId, AchievementDTO achievementDTO) {
//        teamRepository.findById(teamId)
//                .orElseThrow(EntityNotFoundException::new);
//        Achievement achievement = achievementRepository.findById(achId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        Achievement updatedAch = achievementMapper.updateEntity(achievementDTO, achievement);
//        achievementRepository.save(updatedAch);
//
//        return achievementMapper.convertToDTO(updatedAch);
//    }
}
