package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.MatchDTO;
import com.nvd.footballmanager.dto.MatchRequestDTO;
import com.nvd.footballmanager.filters.MatchFilter;
import com.nvd.footballmanager.model.view_statistical.TeamMatch;
import com.nvd.footballmanager.repository.MatchRequestRepository;
import com.nvd.footballmanager.service.StatisticalService;
import com.nvd.footballmanager.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/statistical")
@AllArgsConstructor
public class StatisticalController {
    private final StatisticalService statisticalService;
    private final MatchRequestRepository matchRequestRepository;


    @GetMapping("/matches-by-player")
    public ResponseEntity<CustomApiResponse> getAllMatchesByPlayer(
            @RequestParam UUID memberId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {

        MatchFilter filter = new MatchFilter();
        filter.setMemberId(memberId);
        if (fromDate != null) {
            filter.setFromDate(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        if (toDate != null) {
            filter.setToDate(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        filter.setPageSize(pageSize != null ? pageSize : Constants.DEFAULT_PAGE_SIZE);
        filter.setPageNumber(pageNumber != null ? pageNumber : Constants.DEFAULT_PAGE_NUMBER);
        filter.setSortBy(sortBy);
        filter.setSortDirection(sortDirection);

//        Page<MatchDTO> matches = statisticalService.findAllByPlayerAndTimeRange(filter);
        List<MatchDTO> matches = statisticalService.findAllByPlayerAndTimeRange(filter);
        return ResponseEntity.ok(CustomApiResponse.success(matches));
    }

    @GetMapping("/matches-by-team")
    public ResponseEntity<CustomApiResponse> getAllMatchesByTeam(
            @RequestParam UUID teamId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {
        MatchFilter filter = new MatchFilter();
        filter.setTeamId(teamId);
        if (fromDate != null) {
            filter.setFromDate(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        if (toDate != null) {
            filter.setToDate(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        filter.setPageSize(pageSize != null ? pageSize : Constants.DEFAULT_PAGE_SIZE);
        filter.setPageNumber(pageNumber != null ? pageNumber : Constants.DEFAULT_PAGE_NUMBER);
        filter.setSortBy(sortBy);
        filter.setSortDirection(sortDirection);
        List<TeamMatch> matches = statisticalService.findAllByTeamAndTimeRange(filter);
        return ResponseEntity.ok(CustomApiResponse.success(matches));

    }

    @GetMapping("/matches/upcoming")
    public ResponseEntity<CustomApiResponse> findUpcomingMatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "time") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        Page<MatchRequestDTO> matches = statisticalService.findUpcomingMatches(pageable);
        return ResponseEntity.ok(CustomApiResponse.success(matches));
    }
}
