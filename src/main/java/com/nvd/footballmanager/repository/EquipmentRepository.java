package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.Equipment;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EquipmentRepository extends BaseRepository<Equipment, UUID> {
}
