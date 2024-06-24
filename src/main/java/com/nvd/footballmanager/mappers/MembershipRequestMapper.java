package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.MembershipRequestDTO;
import com.nvd.footballmanager.model.entity.MembershipRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper(componentModel = "spring", uses = {BaseMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MembershipRequestMapper extends BaseMapper<MembershipRequest, MembershipRequestDTO> {

    MembershipRequestMapper INSTANCE = Mappers.getMapper(MembershipRequestMapper.class);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "teamId", source = "team.id")
    @Override
    MembershipRequestDTO convertToDTO(MembershipRequest entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Override
    MembershipRequest convertToEntity(MembershipRequestDTO dto);

    @Override
    default Optional<MembershipRequestDTO> convertOptional(Optional<MembershipRequest> membershipRequest) {
        return membershipRequest.map(this::convertToDTO);
    }

}