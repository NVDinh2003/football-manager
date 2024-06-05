package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.BaseDTO;
import com.nvd.footballmanager.mappers.BaseMapper;
import com.nvd.footballmanager.model.BaseModel;
import com.nvd.footballmanager.repository.BaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.MappedSuperclass;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MappedSuperclass
@RequiredArgsConstructor
public abstract class BaseService<E extends BaseModel,
        DTO extends BaseDTO<ID>,
        ID extends UUID> {


    private final BaseRepository<E, ID> baseRepository;
    private final BaseMapper<E, DTO> baseMapper;

    public List<DTO> findAll() {
        return baseMapper.convertListToDTO(baseRepository.findAll());
    }

    public Optional<DTO> findById(ID id) {
        E e = baseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        return baseMapper.convertOptional(Optional.of(e));
    }

    public DTO create(DTO dto) {

        E e = baseMapper.convertToEntity(dto);

        baseRepository.save(e);
        return baseMapper.convertToDTO(e);
    }

    @Transactional
    public DTO update(ID id, DTO dto) {

        E entityRepo = baseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + id));

        E updated = baseMapper.updateEntity(dto, entityRepo);

        updated.setId(id);

        baseRepository.save(updated);

        return baseMapper.convertToDTO(updated);
    }

    @Transactional
    public void deleteById(ID id) {
        baseRepository.deleteById(id);
    }
}
