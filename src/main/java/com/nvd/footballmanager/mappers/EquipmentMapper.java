package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.EquipmentDTO;
import com.nvd.footballmanager.model.entity.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EquipmentMapper extends BaseMapper<Equipment, EquipmentDTO> {
    @Override
    @Mapping(target = "teamId", source = "team.id")
    EquipmentDTO convertToDTO(Equipment entity);

    @Override
    @Mapping(target = "team.id", ignore = true)
    Equipment convertToEntity(EquipmentDTO dto);

    @Override
    @Mapping(target = "team", ignore = true)
    Equipment updateEntity(EquipmentDTO dto, @MappingTarget Equipment entity);
}
