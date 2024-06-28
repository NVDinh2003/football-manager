package com.nvd.footballmanager.model.entity;

import com.nvd.footballmanager.model.BaseModel;
import com.nvd.footballmanager.model.enums.FinancialRecordType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "financial_records")
public class FinancialRecord extends BaseModel {
    @Enumerated(EnumType.STRING)
    @Column(length = 7, nullable = false, columnDefinition = "enum('INCOME','EXPENSE') NOT NULL")
// giá trị của enum sẽ được lưu dưới dạng string trong db
    private FinancialRecordType type;
    private String note;
    private double amount;
    private LocalDate date;


    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
}
