package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.AchievementDTO;
import com.nvd.footballmanager.dto.TeamDTO;
import com.nvd.footballmanager.model.entity.Achievement;
import com.nvd.footballmanager.model.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TeamMapper.class, BaseMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AchievementMapper extends BaseMapper<Achievement, AchievementDTO> {

    AchievementMapper INSTANCE = Mappers.getMapper(AchievementMapper.class);

    @Mapping(source = "team", target = "team", qualifiedByName = "convertToTeamDTO")
    AchievementDTO convertToDTO(Achievement member);

    @Named("convertToTeamDTO")
    default TeamDTO convertTeamToTeamDTO(Team team) {
        return TeamMapper.INSTANCE.convertToDTO(team);
    }

}
