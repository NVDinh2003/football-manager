package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.MatchRequestDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.filters.MatchRequestFilter;
import com.nvd.footballmanager.service.MatchRequestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/match-requests")
@RequiredArgsConstructor
public class MatchRequestController {
    private final MatchRequestService matchRequestService;

    @PostMapping()
    public ResponseEntity<CustomApiResponse> createMatchRequest(@RequestBody MatchRequestDTO dto) {
        MatchRequestDTO matchRequest = matchRequestService.create(dto);
        return ResponseEntity.ok(CustomApiResponse.created(matchRequest));
    }

    @GetMapping()
    public ResponseEntity<CustomApiResponse> getAll(@ModelAttribute MatchRequestFilter filter) {
        Page<MatchRequestDTO> list = matchRequestService.getAll(filter);
        return ResponseEntity.ok(CustomApiResponse.success(list));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<CustomApiResponse> getAllByTeamId(
            @ModelAttribute MatchRequestFilter filter,
            @PathVariable UUID teamId) {
        filter.setTeamId(teamId);
        Page<MatchRequestDTO> list = matchRequestService.getAll(filter);
        return ResponseEntity.ok(CustomApiResponse.success(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse> getById(@PathVariable("id") UUID id) {

        MatchRequestDTO matchRequestDetails = matchRequestService.getById(id);
        return ResponseEntity.ok(CustomApiResponse.success(matchRequestDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse> delete(@PathVariable("id") UUID id) {
        matchRequestService.deleteById(id);
        return ResponseEntity.ok(CustomApiResponse.noContent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse> update(@PathVariable("id") UUID id,
                                                    @RequestBody MatchRequestDTO dto) {
        MatchRequestDTO updated = matchRequestService.update(id, dto);
        return ResponseEntity.ok(CustomApiResponse.success(updated));
    }

    /**
     * Test chú thích
     * todo: Gợi ý các match request phù hợp với team của user hiện tại
     *
     * @param teamId id của team mà user hiện tại đang là role manager
     * @return list các match request phù hợp
     * @throws EntityNotFoundException nếu không tìm thấy user
     * @throws AccessDeniedException   nếu user hiện tại không là manager của team đó
     */
    @GetMapping("/suggest")
    public ResponseEntity<CustomApiResponse> suggestMatchRequests(@RequestBody UUID teamId) {
        Page<MatchRequestDTO> list = matchRequestService.suggest(teamId);
        return ResponseEntity.ok(CustomApiResponse.success(list));
    }
}
