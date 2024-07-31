package com.nvd.footballmanager.schedules;

import com.nvd.footballmanager.service.MatchService;
import com.nvd.footballmanager.service.MembershipRequestService;
import com.nvd.footballmanager.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalSchedule {
    private final NotificationService notificationService;
    private final MatchService matchService;
    private final MembershipRequestService membershipRequestService;

    private static final String CRON_DELETE_OLD_NOTI = "0 0 0 ? * SUN *";  // midnight every Sunday
    private static final String CRON_SEND_NOTI_EVERY_12_HOURS = "0 0 */12 ? * *";  // every twelve hours

    @Scheduled(cron = CRON_DELETE_OLD_NOTI)
    public void deleteOldNotifications() {
        notificationService.deleteOldNotifications();
        log.info("Deleted old notifications...");
    }

    @Scheduled(cron = CRON_SEND_NOTI_EVERY_12_HOURS)
    public void sendNotiToManagerForConfirmMatchResult() {
        matchService.sendNotiToManagerForConfirmMatchResult();
        log.info("send notification to remind manager for confirm match result...");
    }

    @Scheduled(cron = CRON_SEND_NOTI_EVERY_12_HOURS)
    public void sendNotiToManagerForMembershipRequest() {
        membershipRequestService.sendNotiToManagerForMembershipRequest();
        log.info("send notification to remind manager for membership request...");
    }
}
