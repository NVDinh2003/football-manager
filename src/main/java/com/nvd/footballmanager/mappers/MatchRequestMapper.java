package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.MatchRequestDTO;
import com.nvd.footballmanager.model.entity.MatchRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {BaseMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MatchRequestMapper extends BaseMapper<MatchRequest, MatchRequestDTO> {

}
