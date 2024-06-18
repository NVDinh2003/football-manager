package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.TeamDTO;
import com.nvd.footballmanager.mappers.TeamMapper;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.service.auth.TokenService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TeamService extends BaseService<Team, TeamDTO, UUID> {

    private final TokenService tokenService;

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    protected TeamService(TeamRepository teamRepository, TeamMapper teamMapper, TokenService tokenService) {
        super(teamRepository, teamMapper);
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.tokenService = tokenService;
    }

    @Override
    public TeamDTO create(TeamDTO dto) {
        
        return super.create(dto);
    }
}
