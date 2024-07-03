package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.TeamDTO;
import com.nvd.footballmanager.dto.response.CloudinaryResponse;
import com.nvd.footballmanager.exceptions.BadRequestException;
import com.nvd.footballmanager.mappers.TeamMapper;
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.model.enums.MemberRole;
import com.nvd.footballmanager.repository.MemberRepository;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.repository.UserRepository;
import com.nvd.footballmanager.service.cloud.CloudinaryService;
import com.nvd.footballmanager.utils.Constants;
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
    private final CloudinaryService cloudinaryService;
    private final UserService userService;
    private final UserRepository userRepository;


    protected TeamService(TeamRepository teamRepository,
                          MemberRepository memberRepository, TeamMapper teamMapper,
                          CloudinaryService cloudinaryService,
                          UserService userService,
                          UserRepository userRepository) {
        super(teamRepository, teamMapper);
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.memberRepository = memberRepository;
        this.cloudinaryService = cloudinaryService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional
    public TeamDTO create(TeamDTO dto, MultipartFile logo) {
        UUID currentUserId = userService.getCurrentUser().getId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        memberRepository.findByRoleAndUserId(MemberRole.MANAGER, currentUserId)
                .orElseThrow(() -> new BadRequestException(Constants.ALREADY_MANAGER_OF_TEAM));

        if (logo != null) {
            FileUploadUtil.assertAllowed(logo, FileUploadUtil.IMAGE_PATTERN);
            final String fileName = FileUploadUtil.getFileName(logo.getOriginalFilename());
            final CloudinaryResponse response = cloudinaryService.uploadImage(logo, "team/logo/" + fileName);
            dto.setLogo(response.getUrl());
        }

        Team savedTeam = teamRepository.save(teamMapper.convertToEntity(dto));
        // Create a new member entity with role MANAGER (cuz this user created this team)
        Member manager = new Member();
        manager.setUser(user);
        manager.setTeam(savedTeam);
        manager.setRole(MemberRole.MANAGER);
        memberRepository.save(manager);

        return teamMapper.convertToDTO(savedTeam);
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
