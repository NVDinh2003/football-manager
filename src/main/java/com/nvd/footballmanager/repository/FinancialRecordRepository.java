package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.FinancialRecordFilter;
import com.nvd.footballmanager.model.entity.FinancialRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FinancialRecordRepository extends BaseRepository<FinancialRecord, FinancialRecordFilter, UUID> {

    @Override
    @Query("""
            SELECT fr FROM FinancialRecord fr 
            WHERE ( :#{#filter.s == null || #filter.s.isEmpty()} = TRUE
                OR LOWER(fr.note) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
            )
            AND (:#{#filter.teamId == NULL} = TRUE OR fr.team.id = :#{#filter.teamId}) 
            AND (:#{#filter.type == NULL} = TRUE OR fr.type = :#{#filter.type}) 
            AND (:#{#filter.fromDate == NULL} = TRUE OR fr.date >= :#{#filter.fromDate})
            AND (:#{#filter.toDate == NULL} = TRUE OR fr.date <= :#{#filter.toDate})
            """)
    Page<FinancialRecord> findAllWithFilter(Pageable pageable, FinancialRecordFilter filter);
}
