package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.MatchDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.mappers.MatchMapper;
import com.nvd.footballmanager.model.entity.Match;
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.model.enums.MemberRole;
import com.nvd.footballmanager.repository.MatchRepository;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.service.auth.MailService;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class MatchService extends BaseService<Match, MatchDTO, UUID> {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final MemberService memberService;
    private final UserService userService;
    private final TeamRepository teamRepository;
    private final MailService mailService;
    private final NotificationService notificationService;

    public MatchService(MatchRepository matchRepository, MatchMapper matchMapper,
                        MemberService memberService, UserService userService,
                        TeamRepository teamRepository, MailService mailService,
                        NotificationService notificationService) {
        super(matchRepository, matchMapper);
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
        this.memberService = memberService;
        this.userService = userService;
        this.teamRepository = teamRepository;
        this.mailService = mailService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public MatchDTO create(MatchDTO dto) {
        Optional<Member> manager = memberService.currentUserMemberInTeam(dto.getTeam1Id());
        if (!(manager.isPresent() && manager.get().getRole().equals(MemberRole.MANAGER)))
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);

        Team team2 = teamRepository.findById(dto.getTeam2Id())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Match match = matchMapper.convertToEntity(dto);
        match.setTeam1(manager.get().getTeam());
        match.setTeam2(team2);

        mailService.sendMail("admin@example.com", "New match created", "New match has been created between teams: " +
                manager.get().getTeam().getName() + " and " + team2.getName());

        
        Match savedMatch = matchRepository.save(match);
        return matchMapper.convertToDTO(savedMatch);
    }
}
