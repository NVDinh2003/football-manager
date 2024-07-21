package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.FinancialRecordDTO;
import com.nvd.footballmanager.filters.FinancialRecordFilter;
import com.nvd.footballmanager.model.entity.FinancialRecord;
import com.nvd.footballmanager.model.enums.FinancialRecordType;
import com.nvd.footballmanager.service.FinancialRecordService;
import com.nvd.footballmanager.utils.Constants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/financial-records")
public class FinancialRecordController extends BaseController<FinancialRecord, FinancialRecordDTO, FinancialRecordFilter, UUID> {

    private final FinancialRecordService financialService;

    protected FinancialRecordController(FinancialRecordService financialService) {
        super(financialService);
        this.financialService = financialService;
    }


    @GetMapping("/team/{teamId}")
    public ResponseEntity<CustomApiResponse> findAllByTeam(
//            @ModelAttribute FinancialRecordFilter filter,
            @PathVariable("teamId") UUID teamId,
            @RequestParam(required = false) FinancialRecordType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {

        FinancialRecordFilter filter = new FinancialRecordFilter();
        filter.setTeamId(teamId);
        filter.setType(type);
        filter.setFromDate(fromDate);
        filter.setToDate(toDate);
        filter.setPageSize(pageSize != null ? pageSize : Constants.DEFAULT_PAGE_SIZE);
        filter.setPageNumber(pageNumber != null ? pageNumber : Constants.DEFAULT_PAGE_NUMBER);
        filter.setSortBy(sortBy);
        filter.setSortDirection(sortDirection);

        return ResponseEntity.ok(CustomApiResponse.success(financialService.findAllByTeam(filter)));
    }


}
