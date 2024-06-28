package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.TeamDTO;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.service.TeamService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
public class TeamController extends BaseController<Team, TeamDTO, UUID> {

    private final TeamService teamService;

    protected TeamController(TeamService teamService) {
        super(teamService);
        this.teamService = teamService;
    }

<<<<<<< Updated upstream
=======
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // kiểu 'multipart/form-data'
    public ResponseEntity<CustomApiResponse> create(@RequestPart("team") @Valid TeamDTO teamDTO,
                                                    @RequestPart(value = "logo", required = false) MultipartFile logo) {
        UserDTO currentUser = userService.getCurrentUser();
        TeamDTO createdTeam = teamService.create(teamDTO, logo, currentUser);
        return ResponseEntity.ok(CustomApiResponse.created(createdTeam));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomApiResponse> update(@PathVariable("id") UUID id,
                                                    @RequestPart("team") @Valid TeamDTO teamDTO,
                                                    @RequestPart(value = "logo", required = false) MultipartFile logo) {
        if (memberService.isNotManagerPermission(id)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }
        TeamDTO createdTeam = teamService.update(id, teamDTO, logo);
        return ResponseEntity.ok(CustomApiResponse.created(createdTeam));
    }

    @PostMapping("/{teamId}/members/{userId}")
    public ResponseEntity<CustomApiResponse> addMember(@PathVariable("teamId") UUID teamId,
                                                       @RequestBody @Valid MemberDTO memberDTO,
                                                       @PathVariable("userId") UUID userId) {
        if (memberService.isNotManagerPermission(teamId)) {
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
    public ResponseEntity<CustomApiResponse> getAllMembers(@PathVariable("teamId") UUID teamId) {
        return ResponseEntity.ok(CustomApiResponse.success(memberService.getAllMembers(teamId)));
    }

    @PostMapping("/{id}/achievements")
    public ResponseEntity<CustomApiResponse> addAchievement(@PathVariable("id") UUID teamId,
                                                            @RequestBody @Valid AchievementDTO achievementDTO) {
        if (memberService.isNotManagerPermission(teamId)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }
        AchievementDTO achievement = achievementService.addAchievement(teamId, achievementDTO);
        return ResponseEntity.ok(CustomApiResponse.success(achievement));
    }

    @GetMapping("/{id}/achievements")
    public ResponseEntity<CustomApiResponse> getAllAchievements(@PathVariable("id") UUID teamId) {
        return ResponseEntity.ok(CustomApiResponse.success(achievementService.getAllAchievements(teamId)));
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
>>>>>>> Stashed changes

}
