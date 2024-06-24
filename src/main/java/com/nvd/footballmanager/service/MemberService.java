package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.MemberDTO;
import com.nvd.footballmanager.dto.TeamDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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


    public boolean isNotManagerPermission(UUID teamId) {
        Optional<Member> member = memberRepository.findByUserIdAndTeamId(userService.getCurrentUser().getId(), teamId);
        return !(member.isPresent() && member.get().getRole().equals(MemberRole.MANAGER));
    }


    @Transactional
    public MemberDTO addMember(UUID teamId, UUID userId, MemberDTO memberDTO) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        // chekc if user is already a member of team
        Optional<Member> existingMember = memberRepository.findByUserIdAndTeamId(userId, teamId);
        if (existingMember.isPresent()) {
            throw new IllegalArgumentException(Constants.ALLREADY_MEMBER);
        }

        Member member = memberMapper.convertToEntity(memberDTO);
        member.setTeam(team);
        member.setUser(user);

        Member savedMember = memberRepository.save(member);

        return memberMapper.convertToDTO(savedMember);
    }

    public MemberDTO getMemberInfo(UUID teamId, UUID memberId) {
        teamRepository.findById(teamId)
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByIdAndTeamId(memberId, teamId)
                .orElseThrow(EntityNotFoundException::new);

        return memberMapper.convertToDTO(member);
    }

    public TeamDTO removeMember(UUID teamId, UUID memberId) {

        if (isNotManagerPermission(teamId)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByIdAndTeamId(memberId, teamId)
                .orElseThrow(EntityNotFoundException::new);

        UUID userId = member.getUser().getId();

        // xóa luôn MembershipRequest của user mới remove để test lại send request
        MembershipRequest request = requestRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(EntityNotFoundException::new);
        requestRepository.delete(request);

        memberRepository.delete(member);

        return teamMapper.convertToDTO(team);
    }

    public List<MemberDTO> getAllMembers(UUID teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(EntityNotFoundException::new);
        List<Member> listMembers = memberRepository.findAllByTeamId(teamId);
        return memberMapper.convertListToDTO(listMembers);
    }

    @Transactional
    public MemberDTO updateMemberInfo(UUID teamId, UUID memberId, MemberDTO memberDTO) {
        teamRepository.findById(teamId)
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByIdAndTeamId(memberId, teamId)
                .orElseThrow(EntityNotFoundException::new);


        Member updatedMember = memberMapper.updateEntity(memberDTO, member);
        memberRepository.save(updatedMember);

        return memberMapper.convertToDTO(updatedMember);
    }


}
