package com.nvd.footballmanager.dto;

import com.nvd.footballmanager.model.enums.EquipmentCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {
    private UUID id;
    private String name;
    private int quantity;
    //  "enum('NEW','GOOD','WORN','BROKEN') DEFAULT 'NEW'"
    private EquipmentCondition condition;
    private UUID teamId;
}
