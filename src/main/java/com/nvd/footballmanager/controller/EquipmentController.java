package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.EquipmentDTO;
import com.nvd.footballmanager.filters.EquipmentFilter;
import com.nvd.footballmanager.model.entity.Equipment;
import com.nvd.footballmanager.service.EquipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController extends BaseController<Equipment, EquipmentDTO, EquipmentFilter, UUID> {
    private final EquipmentService equipmentService;

    protected EquipmentController(EquipmentService equipmentService) {
        super(equipmentService);
        this.equipmentService = equipmentService;
    }

    @GetMapping("/t/{teamId}")
    public ResponseEntity<CustomApiResponse> findAllByTeam(
            @ModelAttribute EquipmentFilter filter,
            @PathVariable("teamId") UUID teamId) {
        filter.setTeamId(teamId);
        return ResponseEntity.ok(CustomApiResponse.success(equipmentService.findAllByTeam(filter)));
    }

}
