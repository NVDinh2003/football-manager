package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.MatchDTO;
import com.nvd.footballmanager.model.entity.Match;
import com.nvd.footballmanager.service.MatchService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
public class MatchController extends BaseController<Match, MatchDTO, UUID> {
    private final MatchService matchService;

    protected MatchController(MatchService matchService) {
        super(matchService);
        this.matchService = matchService;
    }

    
}
