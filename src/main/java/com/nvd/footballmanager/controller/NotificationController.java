package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.notification.MarkNotisReadRequest;
import com.nvd.footballmanager.dto.notification.NotiSendRequest;
import com.nvd.footballmanager.dto.notification.NotificationDTO;
import com.nvd.footballmanager.filters.NotificationFilter;
import com.nvd.footballmanager.model.entity.Notification;
import com.nvd.footballmanager.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController extends BaseController<Notification, NotificationDTO, NotificationFilter, UUID> {

    private final NotificationService notificationService;

    protected NotificationController(NotificationService notificationService) {
        super(notificationService);
        this.notificationService = notificationService;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomApiResponse> findAll(NotificationFilter filter) {
        return super.findAll(filter);
    }

    @PostMapping("/send")
    public ResponseEntity<CustomApiResponse> managerSendNotificationToTeam(@RequestBody @Valid NotiSendRequest notiSendRequest) {
        NotificationDTO notificationDTO = notificationService.managerSendNotificationToTeam(notiSendRequest);
        return ResponseEntity.ok(CustomApiResponse.created(notificationDTO));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<CustomApiResponse> getNotificationsByTeamId(
            @ModelAttribute NotificationFilter filter,
            @PathVariable UUID teamId) {
        filter.setTeamId(teamId);
        return ResponseEntity.ok(CustomApiResponse.success(notificationService.getNotificationsByTeam(filter)));
    }

    @PutMapping("/on")
    public ResponseEntity<CustomApiResponse> markNotificationsAsRead(@RequestBody MarkNotisReadRequest request) {
        notificationService.markNotificationsAsRead(request.getTeamId(), request.getNotifyIds());
        return ResponseEntity.ok(CustomApiResponse.success("Notification status updated to 'READ'"));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<CustomApiResponse> getNotificationsByMemberId(@PathVariable UUID memberId) {
        return ResponseEntity.ok(CustomApiResponse.success(notificationService.getNotificationsByMemberId(memberId)));
    }

    @GetMapping("/user")
    public ResponseEntity<CustomApiResponse> getNotificationsByUserId(@ModelAttribute NotificationFilter filter) {
        return ResponseEntity.ok(CustomApiResponse.success(notificationService.getNotificationsByUser(filter)));
    }

    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<CustomApiResponse> deleteAllNotificationsByMemberId(@PathVariable UUID memberId) {
        notificationService.deleteNotificationsByMemberId(memberId);
        return ResponseEntity.ok(CustomApiResponse.success("All notifications deleted"));
    }


}
