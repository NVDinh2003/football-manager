package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.TeamDTO;
import com.nvd.footballmanager.model.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TeamMapper extends BaseMapper<Team, TeamDTO> {
<<<<<<< Updated upstream
    @Override
    Team convertToEntity(TeamDTO teamDTO);

    @Override
    TeamDTO convertToDTO(Team entity);


    @Override
    default Optional<TeamDTO> convertOptional(Optional<Team> team) {
        return team.map(this::convertToDTO);
    }
=======

    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

>>>>>>> Stashed changes
}
