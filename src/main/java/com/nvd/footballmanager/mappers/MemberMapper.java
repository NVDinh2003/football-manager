package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.MemberDTO;
import com.nvd.footballmanager.dto.TeamDTO;
import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TeamMapper.class, BaseMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MemberMapper extends BaseMapper<Member, MemberDTO> {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(source = "user", target = "user", qualifiedByName = "convertToUserDTO")
    @Mapping(source = "team", target = "team", qualifiedByName = "convertToTeamDTO")
    MemberDTO convertToDTO(Member member);

    @Named("convertToUserDTO")
    default UserDTO convertUserToUserDTO(User user) {
        return UserMapper.INSTANCE.convertToDTO(user);
    }

    @Named("convertToTeamDTO")
    default TeamDTO convertTeamToTeamDTO(Team team) {
        return TeamMapper.INSTANCE.convertToDTO(team);
    }

}
