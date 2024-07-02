package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.MembershipRequestDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.mappers.MembershipRequestMapper;
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.entity.MembershipRequest;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.model.enums.MemberRole;
import com.nvd.footballmanager.model.enums.MembershipRequestStatus;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipRequestService {

    private final MembershipRequestRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MembershipRequestMapper membershipRequestMapper;
    private final MemberService memberService;
    private final UserService userService;

    @Transactional
    public MembershipRequestDTO sendMembershipRequest(UUID userId, UUID teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        if (membershipRepository.findByUserIdAndTeamId(userId, teamId).isPresent()) {
            throw new IllegalArgumentException(Constants.ALLREADY_SEND_REQUEST);
        }

        if (memberRepository.findByUserIdAndTeamId(userId, teamId).isPresent()) {
            throw new IllegalArgumentException(Constants.ALLREADY_MEMBER);
        }

        MembershipRequest membershipRequest = MembershipRequest.builder()
                .status(MembershipRequestStatus.PENDING)
                .user(user)
                .team(team)
                .build();

        MembershipRequest saved = membershipRepository.save(membershipRequest);
        return membershipRequestMapper.convertToDTO(saved);

    }

    public List<MembershipRequestDTO> userViewSent(UUID userId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        List<MembershipRequest> listSendRequests = membershipRepository.findAllByUserId(userId);
        return membershipRequestMapper.convertListToDTO(listSendRequests);
    }

    public List<MembershipRequestDTO> managerViewAllReceived(UUID teamId) {
        teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        List<MembershipRequest> listReceivedRequests = membershipRepository.findAllByTeamId(teamId);
        return membershipRequestMapper.convertListToDTO(listReceivedRequests);
    }

    @Transactional
    public List<MembershipRequestDTO> userCancelRequest(UUID membershipRequestId) {
        UUID currentUserId = userService.getCurrentUser().getId();

        MembershipRequest membershipRequest = membershipRepository.findById(membershipRequestId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        if (currentUserId != membershipRequest.getUser().getId()) {
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        }

        membershipRepository.delete(membershipRequest);
        List<MembershipRequest> listSendRequests = membershipRepository.findAllByUserId(currentUserId);
        return membershipRequestMapper.convertListToDTO(listSendRequests);
    }

    @Transactional
    public List<MembershipRequestDTO> managerResponseRequest(UUID membershipRequestId, String status) {

        MembershipRequest membershipRequest = membershipRepository.findById(membershipRequestId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        UUID teamId = membershipRequest.getTeam().getId();
        if (memberService.isNotManagerPermission(teamId)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }


        // update stây tú
        MembershipRequestStatus newStatus = MembershipRequestStatus.valueOf(status);
        membershipRequest.setStatus(newStatus);
        membershipRepository.save(membershipRequest);

        // if request is accepted, add user to team, default role is MEMBER
        if (newStatus == MembershipRequestStatus.ACCEPTED) {
            User user = membershipRequest.getUser();
            Team team = membershipRequest.getTeam();

            Member member = new Member();
            member.setUser(user);
            member.setTeam(team);
            member.setRole(MemberRole.MEMBER);

            memberRepository.save(member);
        }

        List<MembershipRequest> listReceivedRequests = membershipRepository.findAllByTeamId(teamId);
        return membershipRequestMapper.convertListToDTO(listReceivedRequests);
    }
}
