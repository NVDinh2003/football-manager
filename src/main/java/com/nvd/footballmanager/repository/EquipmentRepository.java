package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.EquipmentFilter;
import com.nvd.footballmanager.model.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EquipmentRepository extends BaseRepository<Equipment, EquipmentFilter, UUID> {

    @Override
    @Query("""
            SELECT e FROM Equipment e 
            WHERE ( :#{#filter.s == null || #filter.s.isEmpty()} = TRUE
                OR LOWER(e.name) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
            )
            AND (:#{#filter.teamId == NULL} = TRUE OR e.team.id = :#{#filter.teamId}) 
            AND (:#{#filter.condition == NULL} = TRUE OR e.condition = :#{#filter.condition}) 
            """)
    Page<Equipment> findAllWithFilter(Pageable pageable, EquipmentFilter filter);
}
