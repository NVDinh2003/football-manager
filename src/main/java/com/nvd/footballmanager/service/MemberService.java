package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.MemberDTO;
import com.nvd.footballmanager.dto.TeamDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.exceptions.BadRequestException;
import com.nvd.footballmanager.filters.MemberFilter;
import com.nvd.footballmanager.mappers.MemberMapper;
import com.nvd.footballmanager.mappers.TeamMapper;
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.entity.MembershipRequest;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.model.enums.MemberRole;
import com.nvd.footballmanager.repository.MemberRepository;
import com.nvd.footballmanager.repository.MembershipRequestRepository;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.repository.UserRepository;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final MembershipRequestRepository requestRepository;

    private final MemberMapper memberMapper;
    private final TeamMapper teamMapper;


    @Transactional
    public MemberDTO addMember(UUID teamId, UUID userId, MemberDTO memberDTO) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        if (userService.hasReachedMaxTeams(userId)) {  // check if user has reached max teams limit (3)
            throw new BadRequestException(Constants.MAX_TEAMS_LIMIT_REACHED_MESSAGE);
        }

        // chekc if user is already a member of team
        Optional<Member> existingMember = memberRepository.findByUserIdAndTeamId(userId, teamId);
        if (existingMember.isPresent()) {
            throw new IllegalArgumentException(Constants.ALREADY_MEMBER);
        }

        Member member = memberMapper.convertToEntity(memberDTO);
        member.setTeam(team);
        member.setUser(user);

        Member savedMember = memberRepository.save(member);

        return memberMapper.convertToDTO(savedMember);
    }

    public MemberDTO getMemberInfo(UUID teamId, UUID memberId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Member member = memberRepository.findByIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        return memberMapper.convertToDTO(member);
    }

    @Transactional
    public TeamDTO removeMember(UUID teamId, UUID memberId) {

        if (isCurrentUserNotManagerPermissionOfTeam(teamId)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Member member = memberRepository.findByIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        UUID userId = member.getUser().getId();

        // also delete membership request of this user if exists
        Optional<MembershipRequest> request = requestRepository.findByUserIdAndTeamId(userId, teamId);
        request.ifPresent(requestRepository::delete);

        memberRepository.delete(member);

        return teamMapper.convertToDTO(team);
    }

    public Page<MemberDTO> getAllMembers(MemberFilter filter) {
        teamRepository.findById(filter.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        Page<Member> listMembers = memberRepository.findAllWithFilter(filter.getPageable(), filter);
        return memberMapper.convertPageToDTO(listMembers);
    }

    @Transactional
    public MemberDTO updateMemberInfo(UUID teamId, UUID memberId, MemberDTO memberDTO) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Member member = memberRepository.findByIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));


        Member updatedMember = memberMapper.updateEntity(memberDTO, member);
        memberRepository.save(updatedMember);

        return memberMapper.convertToDTO(updatedMember);
    }

    // check if current user is not manager of team
    public boolean isCurrentUserNotManagerPermissionOfTeam(UUID teamId) {
        Optional<Member> member = memberRepository.findByUserIdAndTeamId(userService.getCurrentUser().getId(), teamId);
        return !(member.isPresent() && member.get().getRole().equals(MemberRole.MANAGER));
    }

    public Optional<Member> currentUserMemberInTeam(UUID teamId) {  // get current user's member info in team
        teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        UUID currentUserId = userService.getCurrentUser().getId();
        return memberRepository.findByUserIdAndTeamId(currentUserId, teamId);
    }

}
