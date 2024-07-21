package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.EquipmentDTO;
import com.nvd.footballmanager.filters.EquipmentFilter;
import com.nvd.footballmanager.model.entity.Equipment;
import com.nvd.footballmanager.model.enums.EquipmentCondition;
import com.nvd.footballmanager.service.EquipmentService;
import com.nvd.footballmanager.utils.Constants;
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

    @GetMapping("/team/{teamId}")
    public ResponseEntity<CustomApiResponse> findAllByTeam(
//            @ModelAttribute EquipmentFilter filter,
            @PathVariable("teamId") UUID teamId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) EquipmentCondition condition,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {
//        filter.setTeamId(teamId);

        EquipmentFilter filter = new EquipmentFilter();
        filter.setTeamId(teamId);
        filter.setName(name);
        filter.setQuantity(quantity);
        filter.setCondition(condition);
        filter.setPageSize(pageSize != null ? pageSize : Constants.DEFAULT_PAGE_SIZE);
        filter.setPageNumber(pageNumber != null ? pageNumber : Constants.DEFAULT_PAGE_NUMBER);
        filter.setSortBy(sortBy);
        filter.setSortDirection(sortDirection);

        return ResponseEntity.ok(CustomApiResponse.success(equipmentService.findAllByTeam(filter)));
    }


}
