package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.EquipmentDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.filters.EquipmentFilter;
import com.nvd.footballmanager.mappers.EquipmentMapper;
import com.nvd.footballmanager.model.entity.Equipment;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.repository.EquipmentRepository;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EquipmentService extends BaseService<Equipment, EquipmentDTO, EquipmentFilter, UUID> {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final MemberService memberService;
    private final TeamRepository teamRepository;


    protected EquipmentService(EquipmentRepository equipmentRepository,
                               EquipmentMapper equipmentMapper,
                               MemberService memberService,
                               TeamRepository teamRepository) {
        super(equipmentRepository, equipmentMapper);
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
        this.memberService = memberService;
        this.teamRepository = teamRepository;
    }

    @Override
    public EquipmentDTO create(EquipmentDTO equipmentDTO) {
        if (memberService.isCurrentUserNotManagerPermissionOfTeam(equipmentDTO.getTeamId()))
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);


        Team team = teamRepository.findById(equipmentDTO.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Equipment equipment = equipmentMapper.convertToEntity(equipmentDTO);
        equipment.setTeam(team);

        Equipment saved = equipmentRepository.save(equipment);
        return equipmentMapper.convertToDTO(saved);
    }

    @Override
    public EquipmentDTO update(UUID uuid, EquipmentDTO equipmentDTO) {
        if (memberService.isCurrentUserNotManagerPermissionOfTeam(equipmentDTO.getTeamId()))
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);

        return super.update(uuid, equipmentDTO);
    }

    @Override
    public void deleteById(UUID uuid) {
        Equipment equipment = equipmentRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        if (memberService.isCurrentUserNotManagerPermissionOfTeam(equipment.getTeam().getId()))
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        super.deleteById(uuid);
    }

    public Page<EquipmentDTO> findAllByTeam(EquipmentFilter filter) {
        teamRepository.findById(filter.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Page<Equipment> list = equipmentRepository.findAllWithFilter(filter.getPageable(), filter);
        return equipmentMapper.convertPageToDTO(list);

    }
}
