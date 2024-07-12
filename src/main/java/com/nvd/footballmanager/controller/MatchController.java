package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.MatchDTO;
import com.nvd.footballmanager.filters.MatchFilter;
import com.nvd.footballmanager.model.entity.Match;
import com.nvd.footballmanager.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
public class MatchController extends BaseController<Match, MatchDTO, MatchFilter, UUID> {
    private final MatchService matchService;

    protected MatchController(MatchService matchService) {
        super(matchService);
        this.matchService = matchService;
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<CustomApiResponse> findAllByTeam(
            @ModelAttribute MatchFilter filter,
            @PathVariable UUID teamId) {
        filter.setTeamId(teamId);
        return ResponseEntity.ok(CustomApiResponse.success(matchService.getAllMatchesByTeam(filter)));
    }

    @PutMapping("/{id}/confirm-result")
    public ResponseEntity<CustomApiResponse> confirmMatchResult(@PathVariable UUID id) {
        return ResponseEntity.ok(CustomApiResponse.success(matchService.confirmMatchResult(id)));
    }


}
