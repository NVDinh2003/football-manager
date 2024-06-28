package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.MemberDTO;
import com.nvd.footballmanager.model.entity.Member;

<<<<<<< Updated upstream
public interface MemberMapper extends BaseMapper<Member, MemberDTO> {
=======
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


>>>>>>> Stashed changes
}
