package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.TeamDTO;
import com.nvd.footballmanager.dto.response.CloudinaryResponse;
import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.mappers.TeamMapper;
import com.nvd.footballmanager.mappers.UserMapper;
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.model.enums.MemberRole;
import com.nvd.footballmanager.repository.MemberRepository;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.service.cloud.CloudinaryService;
import com.nvd.footballmanager.utils.FileUploadUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class TeamService extends BaseService<Team, TeamDTO, UUID> {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamMapper teamMapper;
    private final UserMapper userMapper;
    private final CloudinaryService cloudinaryService;


    protected TeamService(TeamRepository teamRepository,
                          MemberRepository memberRepository, TeamMapper teamMapper,
                          UserMapper userMapper,
                          CloudinaryService cloudinaryService) {
        super(teamRepository, teamMapper);
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.memberRepository = memberRepository;
        this.userMapper = userMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public TeamDTO create(TeamDTO dto, MultipartFile logo, UserDTO current) {

        TeamDTO teamDTO = null;
        if (current != null) {
            if (logo != null) {
                FileUploadUtil.assertAllowed(logo, FileUploadUtil.IMAGE_PATTERN);
                final String fileName = FileUploadUtil.getFileName(logo.getOriginalFilename());
                final CloudinaryResponse response = cloudinaryService.uploadImage(logo, "team/logo/" + fileName);
                dto.setLogo(response.getUrl());
            }

            Team savedTeam = teamRepository.save(teamMapper.convertToEntity(dto));
            // Create a new member entity with role MANAGER (cuz this user created this team)
            Member manager = new Member();
            manager.setUser(userMapper.convertToEntity(current));
            manager.setTeam(savedTeam);
            manager.setRole(MemberRole.MANAGER);
            memberRepository.save(manager);

            teamDTO = teamMapper.convertToDTO(savedTeam);
        }
        return teamDTO;
    }

    @Transactional
    public TeamDTO update(UUID teamId, TeamDTO dto, MultipartFile logo) {

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + teamId));
        if (logo != null) {
            FileUploadUtil.assertAllowed(logo, FileUploadUtil.IMAGE_PATTERN);
            final String fileName = FileUploadUtil.getFileName(logo.getOriginalFilename());
            final CloudinaryResponse response = cloudinaryService.uploadImage(logo, "team/logo/" + fileName);
            dto.setLogo(response.getUrl());
        }

        Team updated = teamMapper.updateEntity(dto, team);

        updated.setId(teamId);

        teamRepository.save(updated);

        return teamMapper.convertToDTO(updated);

    }

}
