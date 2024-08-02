package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.AchievementDTO;
import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.MemberDTO;
import com.nvd.footballmanager.dto.TeamDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.filters.AchievementFilter;
import com.nvd.footballmanager.filters.BaseFilter;
import com.nvd.footballmanager.filters.MemberFilter;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.service.AchievementService;
import com.nvd.footballmanager.service.MemberService;
import com.nvd.footballmanager.service.TeamService;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
public class TeamController extends BaseController<Team, TeamDTO, BaseFilter, UUID> {

    private final TeamService teamService;

    private final MemberService memberService;
    private final AchievementService achievementService;

    protected TeamController(TeamService teamService,
                             MemberService memberService, AchievementService achievementService) {
        super(teamService);
        this.teamService = teamService;
        this.memberService = memberService;
        this.achievementService = achievementService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // kiểu 'multipart/form-data'
    public ResponseEntity<CustomApiResponse> create(@RequestPart("team") @Valid TeamDTO teamDTO,
                                                    @RequestPart(value = "logo", required = false) MultipartFile logo) {
        TeamDTO createdTeam = teamService.create(teamDTO, logo);
        return ResponseEntity.ok(CustomApiResponse.created(createdTeam));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomApiResponse> update(@PathVariable("id") UUID id,
                                                    @RequestPart("team") @Valid TeamDTO teamDTO,
                                                    @RequestPart(value = "logo", required = false) MultipartFile logo) {
        if (memberService.isCurrentUserNotManagerPermissionOfTeam(id)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }
        TeamDTO createdTeam = teamService.update(id, teamDTO, logo);
        return ResponseEntity.ok(CustomApiResponse.created(createdTeam));
    }

    @PostMapping("/{teamId}/members/{userId}")
    public ResponseEntity<CustomApiResponse> addMember(@PathVariable("teamId") UUID teamId,
                                                       @RequestBody @Valid MemberDTO memberDTO,
                                                       @PathVariable("userId") UUID userId) {
        if (memberService.isCurrentUserNotManagerPermissionOfTeam(teamId)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }
        try {
            MemberDTO addedMember = memberService.addMember(teamId, userId, memberDTO);
            return ResponseEntity.ok(CustomApiResponse.success(addedMember));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CustomApiResponse.badRequest(e.getMessage()));
        }
    }

    @PutMapping("{teamId}/members/{memberId}")
    public ResponseEntity<CustomApiResponse> updateMemberInfo(@PathVariable("teamId") UUID teamId,
                                                              @PathVariable("memberId") UUID memberId,
                                                              @RequestBody @Valid MemberDTO memberDTO) {
        MemberDTO member = memberService.updateMemberInfo(teamId, memberId, memberDTO);
        return ResponseEntity.ok(CustomApiResponse.success(member));
    }

    @GetMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<CustomApiResponse> getMemberInfo(@PathVariable("teamId") UUID teamId,
                                                           @PathVariable("memberId") UUID memberId) {
        MemberDTO member = memberService.getMemberInfo(teamId, memberId);
        return ResponseEntity.ok(CustomApiResponse.success(member));
    }

    @DeleteMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<CustomApiResponse> removeMember(@PathVariable("teamId") UUID teamId,
                                                          @PathVariable("memberId") UUID memberId) {
        TeamDTO team = memberService.removeMember(teamId, memberId);
        return ResponseEntity.ok(CustomApiResponse.success(team));
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<CustomApiResponse> getAllMembers(
            @ModelAttribute MemberFilter filter,
            @PathVariable("teamId") UUID teamId) {
        filter.setTeamId(teamId);
        return ResponseEntity.ok(CustomApiResponse.success(memberService.getAllMembers(filter)));
    }


    @PostMapping("/{id}/achievements")
    public ResponseEntity<CustomApiResponse> addAchievement(@PathVariable("id") UUID teamId,
                                                            @RequestBody @Valid AchievementDTO achievementDTO) {
        if (memberService.isCurrentUserNotManagerPermissionOfTeam(teamId)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }
        AchievementDTO achievement = achievementService.addAchievement(teamId, achievementDTO);
        return ResponseEntity.ok(CustomApiResponse.success(achievement));
    }

    @GetMapping("/{id}/achievements")
    public ResponseEntity<CustomApiResponse> getAllAchievements(
            AchievementFilter filter,
            @PathVariable("id") UUID teamId) {
        filter.setTeamId(teamId);
        return ResponseEntity.ok(CustomApiResponse.success(achievementService.getAllAchievements(filter)));
    }

    @PutMapping("{teamId}/achievements/{achId}")
    public ResponseEntity<CustomApiResponse> updateAchievement(@PathVariable("teamId") UUID teamId,
                                                               @PathVariable("achId") UUID achId,
                                                               @RequestBody @Valid AchievementDTO achievementDTO) {
        teamService.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        AchievementDTO achievement = achievementService.update(achId, achievementDTO);
        return ResponseEntity.ok(CustomApiResponse.success(achievement));
    }


}
