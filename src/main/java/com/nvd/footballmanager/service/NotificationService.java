package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.notification.NotiSendRequest;
import com.nvd.footballmanager.dto.notification.NotificationDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.mappers.NotificationMapper;
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.entity.MemberNotification;
import com.nvd.footballmanager.model.entity.Notification;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.model.enums.MemberRole;
import com.nvd.footballmanager.repository.MemberNotificationRepository;
import com.nvd.footballmanager.repository.MemberRepository;
import com.nvd.footballmanager.repository.NotificationRepository;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class NotificationService extends BaseService<Notification, NotificationDTO, UUID> {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final MemberNotificationRepository memberNotificationRepository;
    private final TeamRepository teamRepository;

    protected NotificationService(NotificationRepository notificationRepository,
                                  NotificationMapper notificationMapper,
                                  MemberService memberService,
                                  MemberNotificationRepository memberNotificationRepository,
                                  MemberRepository memberRepository,
                                  TeamRepository teamRepository) {
        super(notificationRepository, notificationMapper);
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.memberNotificationRepository = memberNotificationRepository;

        this.teamRepository = teamRepository;
    }

    @Transactional
    public NotificationDTO managerSendNotificationToTeam(NotiSendRequest noti) {

        Optional<Member> manager = memberService.currentUserMemberInTeam(noti.getTeamId());
        if (!(manager.isPresent() && manager.get().getRole().equals(MemberRole.MANAGER)))
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);

        Team team = teamRepository.findById(noti.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Notification notification = new Notification();
        notification.setTitle(noti.getTitle());
        notification.setContent(noti.getContent());
        notification.setSender(manager.get());
        notification.setTeam(team);

        notification = notificationRepository.save(notification);

        List<Member> teamMembers = memberRepository.findAllByTeamId(noti.getTeamId());

        Set<MemberNotification> recipients = new HashSet<>();
        for (Member member : teamMembers) {
            if (!member.equals(manager)) {  // -manager
                MemberNotification memberNotification = new MemberNotification();
                memberNotification.setMember(member);
                memberNotification.setNotification(notification);
                memberNotification = memberNotificationRepository.save(memberNotification);
                recipients.add(memberNotification);
            }
        }

        notification.setRecipients(recipients);
        notification = notificationRepository.save(notification);
        return notificationMapper.convertToDTO(notification);
    }

    public List<NotificationDTO> getNotificationsByTeamId(UUID teamId) {
        if (memberService.currentUserMemberInTeam(teamId).isEmpty())
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        return notificationMapper.convertListToDTO(notificationRepository.findByTeamId(teamId));
    }

    @Override
    public Optional<NotificationDTO> findById(UUID id) {
        checkPermissionToReadNoti(id);
        return super.findById(id);
    }


    private void checkPermissionToReadNoti(UUID notiId) {
        Notification noti = notificationRepository.findById(notiId).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        UUID teamId = noti.getTeam().getId();
        if (memberService.currentUserMemberInTeam(teamId).isEmpty())
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
    }

    @Transactional
    public void markNotificationsAsRead(UUID teamId, List<UUID> notifyIds) {
        for (UUID id : notifyIds) {
            checkPermissionToReadNoti(id);
        }

        Member currentMember = memberService.currentUserMemberInTeam(teamId).get();
        List<MemberNotification> memberNotifications = memberNotificationRepository
                .findByMemberAndNotificationIdIn(currentMember, notifyIds);

        for (MemberNotification memberNotification : memberNotifications) {
            memberNotification.setRead(true);
        }

        memberNotificationRepository.saveAll(memberNotifications);
    }

}
