package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.TeamDTO;
import com.nvd.footballmanager.model.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper(componentModel = "spring",
        uses = {BaseMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TeamMapper extends BaseMapper<Team, TeamDTO> {

    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    @Override
    Team convertToEntity(TeamDTO teamDTO);

    @Override
    TeamDTO convertToDTO(Team entity);

    @Override
    default Optional<TeamDTO> convertOptional(Optional<Team> team) {
        return team.map(this::convertToDTO);
    }


}

