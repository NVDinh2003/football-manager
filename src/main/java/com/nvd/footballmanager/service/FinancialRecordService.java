package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.FinancialRecordDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.mappers.FinancialRecordMapper;
import com.nvd.footballmanager.model.entity.FinancialRecord;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.repository.FinancialRecordRepository;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FinancialRecordService extends BaseService<FinancialRecord, FinancialRecordDTO, UUID> {

    private final FinancialRecordRepository financialRepository;
    private final FinancialRecordMapper financialMapper;
    private final MemberService memberService;
    private final TeamRepository teamRepository;

    protected FinancialRecordService(FinancialRecordRepository financialRepository,
                                     FinancialRecordMapper financialMapper,
                                     MemberService memberService, TeamRepository teamRepository) {
        super(financialRepository, financialMapper);
        this.financialRepository = financialRepository;
        this.financialMapper = financialMapper;
        this.memberService = memberService;
        this.teamRepository = teamRepository;
    }

    @Override
    public FinancialRecordDTO create(FinancialRecordDTO financialRecordDTO) {
        if (memberService.isNotManagerPermission(financialRecordDTO.getTeamId()))
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);


        Team team = teamRepository.findById(financialRecordDTO.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        FinancialRecord financialRecord = financialMapper.convertToEntity(financialRecordDTO);
        financialRecord.setTeam(team);

        FinancialRecord saved = financialRepository.save(financialRecord);
        return financialMapper.convertToDTO(saved);
    }

    @Override
    public FinancialRecordDTO update(UUID uuid, FinancialRecordDTO financialRecordDTO) {
        if (memberService.isNotManagerPermission(financialRecordDTO.getTeamId()))
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);

        return super.update(uuid, financialRecordDTO);
    }

    @Override
    public void deleteById(UUID uuid) {
        FinancialRecord financialRecord = financialRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        if (memberService.isNotManagerPermission(financialRecord.getTeam().getId()))
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        super.deleteById(uuid);
    }
}
