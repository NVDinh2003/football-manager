package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.EquipmentDTO;
import com.nvd.footballmanager.model.entity.Equipment;
import com.nvd.footballmanager.service.EquipmentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController extends BaseController<Equipment, EquipmentDTO, UUID> {
    private final EquipmentService equipmentService;

    protected EquipmentController(EquipmentService equipmentService) {
        super(equipmentService);
        this.equipmentService = equipmentService;
    }

}
