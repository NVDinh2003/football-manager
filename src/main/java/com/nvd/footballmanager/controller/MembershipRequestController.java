package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.MembershipRequestDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.service.MemberService;
import com.nvd.footballmanager.service.MembershipRequestService;
import com.nvd.footballmanager.service.UserService;
import com.nvd.footballmanager.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/membership-requests")
@RequiredArgsConstructor
public class MembershipRequestController {
    private final MembershipRequestService membershipRequestService;
    private final MemberService memberService;
    private final UserService userService;

    @PostMapping("/{teamId}")
    public ResponseEntity<CustomApiResponse> sendMembershipRequest(@PathVariable("teamId") UUID teamId) {
        try {
            MembershipRequestDTO membershipRequest = membershipRequestService.sendMembershipRequest(teamId);
            return ResponseEntity.ok(CustomApiResponse.created(membershipRequest));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomApiResponse.badRequest(e.getMessage()));
        }

    }

    @GetMapping("send")
    public ResponseEntity<CustomApiResponse> userViewSent() {
        UUID userId = userService.getCurrentUser().getId();
        List<MembershipRequestDTO> list = membershipRequestService.userViewSent(userId);
        return ResponseEntity.ok(CustomApiResponse.success(list));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<CustomApiResponse> managerViewAllReceived(@PathVariable("teamId") UUID teamId) {
        if (memberService.isCurrentUserNotManagerPermissionOfTeam(teamId)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }
        List<MembershipRequestDTO> list = membershipRequestService.managerViewAllReceived(teamId);
        return ResponseEntity.ok(CustomApiResponse.success(list));
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<CustomApiResponse> userCancelRequest(@PathVariable("id") UUID membershipRequestId) {
        List<MembershipRequestDTO> list = membershipRequestService.userCancelRequest(membershipRequestId);
        return ResponseEntity.ok(CustomApiResponse.success(list));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse> managerResponseRequest(@PathVariable("id") UUID membershipRequestId, @RequestParam String status) {

        List<MembershipRequestDTO> list = membershipRequestService.managerResponseRequest(membershipRequestId, status);
        return ResponseEntity.ok(CustomApiResponse.success(list));
    }
}
