package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.MembershipRequestDTO;
import com.nvd.footballmanager.dto.notification.NotiSendRequest;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.exceptions.BadRequestException;
import com.nvd.footballmanager.mappers.MembershipRequestMapper;
import com.nvd.footballmanager.mappers.UserMapper;
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
    private final UserMapper userMapper;
    private final NotificationService notificationService;

    @Transactional
    public MembershipRequestDTO sendMembershipRequest(UUID teamId) {  // user send request to join team
        UUID userId = userService.getCurrentUser().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        if (userService.hasReachedMaxTeams(userId)) {  // check if user has reached max teams limit (3)
            throw new BadRequestException(Constants.MAX_TEAMS_LIMIT_REACHED_MESSAGE);
        }

        // check if user has already sent request to join team
        if (membershipRepository.findByUserIdAndTeamId(userId, teamId).isPresent()) {
            throw new IllegalArgumentException(Constants.ALREADY_SEND_REQUEST);
        }

        // check if user is already a member of team
        if (memberRepository.findByUserIdAndTeamId(userId, teamId).isPresent()) {
            throw new IllegalArgumentException(Constants.ALREADY_MEMBER);
        }

        MembershipRequest membershipRequest = MembershipRequest.builder()
                .status(MembershipRequestStatus.PENDING)
                .user(user)
                .team(team)
                .build();

        // send noti to manager
        NotiSendRequest noti = NotiSendRequest.builder()
                .title(user.getUsername() + Constants.USER_SEND_MEMBERSHIP_REQUEST)
                .content(userMapper.convertToDTO(user).toString()).build();

        Member manager = memberRepository.findByRoleAndTeamId(MemberRole.MANAGER, teamId);
        notificationService.sendNotiToManager(noti, manager);

        MembershipRequest saved = membershipRepository.save(membershipRequest);
        return membershipRequestMapper.convertToDTO(saved);

    }

    public List<MembershipRequestDTO> userViewSent(UUID userId) { // user view all sent requests
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        List<MembershipRequest> listSendRequests = membershipRepository.findAllByUserId(userId);
        return membershipRequestMapper.convertListToDTO(listSendRequests);
    }

    public List<MembershipRequestDTO> managerViewAllReceived(UUID teamId) {  // manager view all received requests
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
        if (memberService.isCurrentUserNotManagerPermissionOfTeam(teamId)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }


        // update stây tú
        MembershipRequestStatus newStatus = MembershipRequestStatus.valueOf(status);
        membershipRequest.setStatus(newStatus);
        membershipRepository.save(membershipRequest);

        // system send noti to user
        NotiSendRequest noti = new NotiSendRequest();

        // if request is accepted, add user to team, default role is MEMBER
        if (newStatus == MembershipRequestStatus.ACCEPTED) {
            User user = membershipRequest.getUser();
            if (userService.hasReachedMaxTeams(user.getId())) {  // check if user has reached max teams limit (3)
                throw new BadRequestException(Constants.MAX_TEAMS_LIMIT_REACHED_MESSAGE);
            }
            Team team = membershipRequest.getTeam();

            Member member = new Member();
            member.setUser(user);
            member.setTeam(team);
            member.setRole(MemberRole.MEMBER);

            memberRepository.save(member);

            // set notification send to user
            noti.setTitle("Your request to join team " + team.getName() + " has been accepted!");
            noti.setContent("You are now a member of team " + team.getName());
        } else {
            // if request is rejected set notification send to user
            noti.setTitle("Your request to join team " + membershipRequest.getTeam().getName() + " has been rejected!");
            noti.setContent("Your request has been rejected by the team manager");
        }

        notificationService.sendNotiToUser(noti, membershipRequest.getUser());


        List<MembershipRequest> listReceivedRequests = membershipRepository.findAllByTeamId(teamId);
        return membershipRequestMapper.convertListToDTO(listReceivedRequests);
    }
}
