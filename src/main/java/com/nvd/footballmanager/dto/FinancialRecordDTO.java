package com.nvd.footballmanager.dto;

import com.nvd.footballmanager.model.enums.FinancialRecordType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FinancialRecordDTO extends BaseDTO<UUID> {
    private FinancialRecordType type;
    private String note;
    private double amount;
    private LocalDate date;
    private UUID teamId;
}
