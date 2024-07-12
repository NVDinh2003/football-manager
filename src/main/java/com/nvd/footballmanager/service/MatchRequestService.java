package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.MatchRequestDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.exceptions.BadRequestException;
import com.nvd.footballmanager.filters.MatchRequestFilter;
import com.nvd.footballmanager.mappers.MatchRequestMapper;
import com.nvd.footballmanager.model.entity.MatchRequest;
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.model.enums.MatchRequestStatus;
import com.nvd.footballmanager.repository.MatchRequestRepository;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchRequestService {

    private final MatchRequestRepository matchRequestRepository;
    private final MatchRequestMapper matchRequestMapper;
    private final MemberService memberService;
    private final TeamRepository teamRepository;

    @Transactional
    public MatchRequestDTO create(MatchRequestDTO dto) {
        checkPermission(dto.getTeamId());

        MatchRequest matchRequest = matchRequestMapper.convertToEntity(dto);
        matchRequest.setStatus(MatchRequestStatus.NEW);
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        matchRequest.setTeam(team);

        return matchRequestMapper.convertToDTO(matchRequestRepository.save(matchRequest));
    }

    public Page<MatchRequestDTO> getAll(MatchRequestFilter filter) {
        if (filter.getStatus() == null)
            filter.setStatus(MatchRequestStatus.NEW);
        Page<MatchRequest> list = matchRequestRepository.findAllWithFilter(filter.getPageable(), filter);
        return matchRequestMapper.convertPageToDTO(list);
    }

    public MatchRequestDTO getById(UUID id) {
        MatchRequest matchRequest = matchRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        return matchRequestMapper.convertToDTO(matchRequest);
    }

    @Transactional
    public MatchRequestDTO update(UUID id, MatchRequestDTO dto) {
        checkPermission(dto.getTeamId());
        MatchRequest existingMatchRequest = matchRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        matchRequestMapper.updateEntity(dto, existingMatchRequest);
        existingMatchRequest = matchRequestRepository.save(existingMatchRequest);
        return matchRequestMapper.convertToDTO(existingMatchRequest);
    }

    @Transactional
    public void deleteById(UUID id) {
        MatchRequest matchRequest = matchRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        checkPermission(matchRequest.getTeam().getId());
        matchRequestRepository.deleteById(id);
    }

    public void checkPermission(UUID teamId) {     // user must joint team and have manager permission
        Optional<Member> member = memberService.currentUserMemberInTeam(teamId);
        if (member.isEmpty()) {     // If the current user doesn't join any team
            throw new BadRequestException(Constants.NOT_JOIN_ANY_TEAM);
        } else if (memberService.isCurrentUserNotManagerPermissionOfTeam(teamId)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION); // Check if the current user is a manager of the team
        }
    }

    public Page<MatchRequestDTO> suggest(UUID teamId) {  // suggest match request for user (is manager of team)
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        if (memberService.isCurrentUserNotManagerPermissionOfTeam(teamId)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }

        MatchRequestFilter filter = new MatchRequestFilter();
        filter.setLocationDetails(team.getAddress());  // maybe update the address details later
        filter.setStatus(MatchRequestStatus.NEW);
        filter.setRankPoints(team.getRankPoints());
        filter.setRankPointRange(Constants.RANK_POINT_RANGE); // range of rank point: +-6 points
        filter.setTeamId(teamId);

        Page<MatchRequest> list = matchRequestRepository.suggest(filter.getPageable(), filter);
        return matchRequestMapper.convertPageToDTO(list);
    }
}
