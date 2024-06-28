package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.FinancialRecord;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FinancialRecordRepository extends BaseRepository<FinancialRecord, UUID> {
    
}
