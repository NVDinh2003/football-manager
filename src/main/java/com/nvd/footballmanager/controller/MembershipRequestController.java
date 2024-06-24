package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.MembershipRequestDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.service.MemberService;
import com.nvd.footballmanager.service.MembershipRequestService;
import com.nvd.footballmanager.service.UserService;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
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

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<String> entityNotFound() {
        return new ResponseEntity<>(Constants.ENTITY_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<String> notManagerPermission(AccessDeniedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @PostMapping("/{teamId}")
    public ResponseEntity<CustomApiResponse> sendMembershipRequest(@PathVariable("teamId") UUID teamId) {
        try {
            UUID userId = userService.getCurrentUser().getId();
            MembershipRequestDTO membershipRequest = membershipRequestService.sendMembershipRequest(userId, teamId);
            return ResponseEntity.ok(CustomApiResponse.created(membershipRequest));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CustomApiResponse.badRequest(e.getMessage()));
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
        if (memberService.isNotManagerPermission(teamId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(CustomApiResponse.forbidden(Constants.NOT_MANAGER_PERMISSION));
        }
        List<MembershipRequestDTO> list = membershipRequestService.managerViewAllReceived(teamId);
        return ResponseEntity.ok(CustomApiResponse.success(list));
    }

    @DeleteMapping("/cancel/{Id}")
    public ResponseEntity<CustomApiResponse> userCancelRequest(@PathVariable("Id") UUID membershipRequestId) {
        List<MembershipRequestDTO> list = membershipRequestService.userCancelRequest(membershipRequestId);
        return ResponseEntity.ok(CustomApiResponse.success(list));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse> managerResponseRequest(@PathVariable("id") UUID membershipRequestId, @RequestParam String status) {

        List<MembershipRequestDTO> list = membershipRequestService.managerResponseRequest(membershipRequestId, status);
        return ResponseEntity.ok(CustomApiResponse.success(list));
    }
}

