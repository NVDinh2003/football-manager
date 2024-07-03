package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.notification.MarkNotisReadRequest;
import com.nvd.footballmanager.dto.notification.NotiSendRequest;
import com.nvd.footballmanager.dto.notification.NotificationDTO;
import com.nvd.footballmanager.exceptions.BadRequestException;
import com.nvd.footballmanager.model.entity.Notification;
import com.nvd.footballmanager.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController extends BaseController<Notification, NotificationDTO, UUID> {

    private final NotificationService notificationService;

    protected NotificationController(NotificationService notificationService) {
        super(notificationService);
        this.notificationService = notificationService;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomApiResponse> findAll() {
        return super.findAll();
    }

    @PostMapping("/send")
    public ResponseEntity<CustomApiResponse> managerSendNotificationToTeam(@RequestBody @Valid NotiSendRequest notiSendRequest) {
        NotificationDTO notificationDTO = notificationService.managerSendNotificationToTeam(notiSendRequest);
        return ResponseEntity.ok(CustomApiResponse.created(notificationDTO));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<CustomApiResponse> getNotificationsByTeamId(@PathVariable UUID teamId) {
        return ResponseEntity.ok(CustomApiResponse.success(notificationService.getNotificationsByTeamId(teamId)));
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
    public ResponseEntity<CustomApiResponse> getNotificationsByUserId() {
        return ResponseEntity.ok(CustomApiResponse.success(notificationService.getNotificationsByUser()));
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<CustomApiResponse> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomApiResponse
                .notFound(ex.getMessage()));
    }

}
