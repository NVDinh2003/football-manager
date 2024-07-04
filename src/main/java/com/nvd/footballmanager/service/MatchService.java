package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.MatchDTO;
import com.nvd.footballmanager.dto.notification.NotiSendRequest;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.mappers.MatchMapper;
import com.nvd.footballmanager.model.entity.Match;
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.model.enums.MemberRole;
import com.nvd.footballmanager.repository.MatchRepository;
import com.nvd.footballmanager.repository.MemberRepository;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.service.auth.MailService;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class MatchService extends BaseService<Match, MatchDTO, UUID> {

    @Value("${app.base.url}")
    private String baseUrl;
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final MemberService memberService;
    private final UserService userService;
    private final TeamRepository teamRepository;
    private final MailService mailService;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;

    public MatchService(MatchRepository matchRepository, MatchMapper matchMapper,
                        MemberService memberService, UserService userService,
                        TeamRepository teamRepository, MailService mailService,
                        NotificationService notificationService,
                        MemberRepository memberRepository) {
        super(matchRepository, matchMapper);
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
        this.memberService = memberService;
        this.userService = userService;
        this.teamRepository = teamRepository;
        this.mailService = mailService;
        this.notificationService = notificationService;
        this.memberRepository = memberRepository;
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
        Set<Member> teamMembers = new HashSet<>(memberRepository.findAllByTeamId(dto.getTeam1Id()));
        match.setMembers(teamMembers);

        // manager send noti to team members
        NotiSendRequest notiToTeamMembers = new NotiSendRequest();
        notiToTeamMembers.setTeamId(manager.get().getTeam().getId());
        notiToTeamMembers.setTitle("New match created");
        notiToTeamMembers.setContent("New match has been created between teams: " +
                manager.get().getTeam().getName() + " and " + team2.getName() + " at " + match.getVenue()
                + " on " + match.getTime());
        notificationService.managerSendNotificationToTeam(notiToTeamMembers);

        Match savedMatch = matchRepository.save(match);
        return matchMapper.convertToDTO(savedMatch);
    }

    @Override
    @Transactional
    public MatchDTO update(UUID id, MatchDTO dto) {

        Optional<Member> manager = memberService.currentUserMemberInTeam(dto.getTeam1Id());
        if (!(manager.isPresent() && manager.get().getRole().equals(MemberRole.MANAGER)))
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);

        Team team2 = teamRepository.findById(dto.getTeam2Id())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        // send updated noti to team members
        NotiSendRequest notiToTeamMembers = new NotiSendRequest();
        notiToTeamMembers.setTeamId(manager.get().getTeam().getId());
        notiToTeamMembers.setTitle("Manager updated match information.");
        StringBuilder content = new StringBuilder("Match has been updated between teams: " +
                manager.get().getTeam().getName() + " and " + team2.getName() + " at " + dto.getVenue()
                + " on " + dto.getTime());

        Instant now = Instant.now();  // check if match has ended, update result
        if (now.isAfter(match.getTime())) {
            content.append(" with score: ").append(dto.getTeam1Scored()).append(" - ").append(dto.getTeam2Scored());

            // send noti comfirm result to opponent manager
            NotiSendRequest notiToOpponentManager = new NotiSendRequest();
            notiToOpponentManager.setTitle("Match result confirmation.");
            String contentResult = String.format("Match between your team and %s has ended with score: %s %s - %s %s. Please confirm the result by click this link: %s/api/matches/%s/confirm-result",
                    manager.get().getTeam().getName(),
                    team2.getName(),
                    dto.getTeam2Scored(),
                    manager.get().getTeam().getName(),
                    dto.getTeam1Scored(),
                    baseUrl,
                    id);
            notiToOpponentManager.setContent(contentResult);

            Member opponentManager = memberRepository.findByRoleAndTeamId(MemberRole.MANAGER, team2.getId());
            notificationService.sendNotiToManager(notiToOpponentManager, opponentManager);

        }
        notiToTeamMembers.setContent(content.toString());
        notificationService.managerSendNotificationToTeam(notiToTeamMembers);

//        updated.setId(teamId);
        Match updated = matchMapper.updateEntity(dto, match);
        updated.setTeam2(team2);  // update opponent team if it changes

        matchRepository.save(updated);

        return matchMapper.convertToDTO(updated);
    }

    public List<MatchDTO> getAllMatchesByTeam(UUID teamId) {
        if (memberService.currentUserMemberInTeam(teamId).isEmpty())
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        return matchMapper.convertListToDTO(matchRepository.findAllByTeam1Id(teamId));
    }

    public MatchDTO confirmMatchResult(UUID id) {
        // lúc này manager thực hiện confirm này là manager của team2
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        if (match.isConfirmed())
            throw new AccessDeniedException(Constants.MATCH_ALREADY_COMFIRMED);

        match.setConfirmed(true);
        Team team1 = match.getTeam1();
        Team team2 = match.getTeam2();
        if (match.getTeam1Scored() > match.getTeam2Scored())    // team 1 win
            team1.setRankPoints(team1.getRankPoints() + 3);
        else if (match.getTeam1Scored() < match.getTeam2Scored())   // team 2 win
            team2.setRankPoints(team2.getRankPoints() + 3);
        else {  // draw
            team1.setRankPoints(team1.getRankPoints() + 1);
            team2.setRankPoints(team2.getRankPoints() + 1);
        }

        Match updatedMatch = matchRepository.save(match);
        teamRepository.save(team1);
        teamRepository.save(team2);

        return matchMapper.convertToDTO(updatedMatch);
    }
}
