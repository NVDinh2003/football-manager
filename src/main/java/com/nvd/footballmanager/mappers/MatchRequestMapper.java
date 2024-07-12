package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.MatchRequestDTO;
import com.nvd.footballmanager.model.entity.MatchRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {BaseMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MatchRequestMapper extends BaseMapper<MatchRequest, MatchRequestDTO> {

    @Override
    @Mapping(target = "teamId", source = "team.id")
    MatchRequestDTO convertToDTO(MatchRequest entity);

    @Override
    @Mapping(target = "team.id", ignore = true)
    MatchRequest convertToEntity(MatchRequestDTO dto);

    @Override
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "feed", ignore = true)
    MatchRequest updateEntity(MatchRequestDTO dto, @MappingTarget MatchRequest entity);
}
