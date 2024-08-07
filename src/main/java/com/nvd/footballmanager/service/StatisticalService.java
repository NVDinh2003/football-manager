package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.MatchDTO;
import com.nvd.footballmanager.dto.MatchRequestDTO;
import com.nvd.footballmanager.filters.MatchFilter;
import com.nvd.footballmanager.mappers.MatchMapper;
import com.nvd.footballmanager.mappers.MatchRequestMapper;
import com.nvd.footballmanager.model.entity.Match;
import com.nvd.footballmanager.model.entity.MatchRequest;
import com.nvd.footballmanager.model.view_statistical.TeamMatch;
import com.nvd.footballmanager.repository.MatchRepository;
import com.nvd.footballmanager.repository.MatchRequestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StatisticalService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final MatchRequestRepository matchRequestRepository;
    private final MatchRequestMapper matchRequestMapper;

    //    public Page<MatchDTO>
    public List<MatchDTO>
    findAllByPlayerAndTimeRange(MatchFilter filter) {
        long startTime = System.currentTimeMillis();

//        Page<Match>
        List<Match> list = matchRepository.findAllByPlayerAndTimeRange(filter);

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        log.info("Query time: " + duration + " ms");
//        return matchMapper.convertPageToDTO(list);
        return matchMapper.convertListToDTO(list);
    }

    public List<TeamMatch> findAllByTeamAndTimeRange(MatchFilter filter) {
        long startTime = System.currentTimeMillis();

        List<TeamMatch> list = matchRepository.findAllByTeamAndTimeRange(filter);

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        log.info("Query time: " + duration + " ms");
//        return matchMapper.convertListToDTO(list);
        return list;
    }

    public Page<MatchRequestDTO> findUpcomingMatches(Pageable pageable) {
        Page<MatchRequest> list = matchRequestRepository.findUpcomingMatches(pageable);
        return matchRequestMapper.convertPageToDTO(list);
    }
}

