package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.FinancialRecordDTO;
import com.nvd.footballmanager.filters.FinancialRecordFilter;
import com.nvd.footballmanager.model.entity.FinancialRecord;
import com.nvd.footballmanager.service.FinancialRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/financial-records")
public class FinancialRecordController extends BaseController<FinancialRecord, FinancialRecordDTO, FinancialRecordFilter, UUID> {

    private final FinancialRecordService financialService;

    protected FinancialRecordController(FinancialRecordService financialService) {
        super(financialService);
        this.financialService = financialService;
    }


    @GetMapping("/t/{teamId}")
    public ResponseEntity<CustomApiResponse> findAllByTeam(
            @ModelAttribute FinancialRecordFilter filter,
            @PathVariable("teamId") UUID teamId) {
        filter.setTeamId(teamId);
        return ResponseEntity.ok(CustomApiResponse.success(financialService.findAllByTeam(filter)));
    }

}
