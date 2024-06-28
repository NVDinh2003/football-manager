package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.FinancialRecordDTO;
import com.nvd.footballmanager.model.entity.FinancialRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FinancialRecordMapper extends BaseMapper<FinancialRecord, FinancialRecordDTO> {

    @Override
    @Mapping(target = "teamId", source = "team.id")
    FinancialRecordDTO convertToDTO(FinancialRecord entity);

    @Override
    @Mapping(target = "team.id", ignore = true)
    FinancialRecord convertToEntity(FinancialRecordDTO dto);

    @Override
    @Mapping(target = "team", ignore = true)
    FinancialRecord updateEntity(FinancialRecordDTO dto, @MappingTarget FinancialRecord entity);
}