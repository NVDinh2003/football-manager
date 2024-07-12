package com.nvd.footballmanager.filters;

import com.nvd.footballmanager.model.enums.EquipmentCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentFilter extends BaseFilter {
    private UUID teamId;
    private String name;
    private int quantity;
    private EquipmentCondition condition;
}
