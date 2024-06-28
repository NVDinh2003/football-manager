package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.FinancialRecordDTO;
import com.nvd.footballmanager.model.entity.FinancialRecord;
import com.nvd.footballmanager.service.FinancialRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/financial-records")
public class FinancialRecordController extends BaseController<FinancialRecord, FinancialRecordDTO, UUID> {

    private final FinancialRecordService financialService;

    protected FinancialRecordController(FinancialRecordService financialService) {
        super(financialService);
        this.financialService = financialService;
    }

}
