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


}
