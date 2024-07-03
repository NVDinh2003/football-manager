package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.MatchDTO;
import com.nvd.footballmanager.model.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MatchMapper extends BaseMapper<Match, MatchDTO> {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Override
    @Mapping(target = "team1Id", source = "team1.id")
    @Mapping(target = "team2Id", source = "team2.id")
    MatchDTO convertToDTO(Match match);

    @Override
    @Mapping(target = "team1", ignore = true)
    @Mapping(target = "team2", ignore = true)
    Match convertToEntity(MatchDTO matchDTO);
}
