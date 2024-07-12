package com.nvd.footballmanager.service;

import com.nvd.footballmanager.filters.BaseFilter;
import com.nvd.footballmanager.mappers.BaseMapper;
import com.nvd.footballmanager.repository.BaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.MappedSuperclass;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseService<E, DTO,
        FT extends BaseFilter,
        ID extends UUID> {

    private final BaseRepository<E, FT, ID> baseRepository;
    private final BaseMapper<E, DTO> baseMapper;


    protected BaseService(BaseRepository<E, FT, ID> baseRepository, BaseMapper<E, DTO> baseMapper) {
        this.baseRepository = baseRepository;
        this.baseMapper = baseMapper;
    }

//    public List<DTO> findAll() {
//        return baseMapper.convertListToDTO(baseRepository.findAll());
//    }

    public Page<DTO> findAll(FT filter) {
        return baseMapper.convertPageToDTO(baseRepository.findAllWithFilter(filter.getPageable(), filter));
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

//        updated.setId(id);
        baseRepository.save(updated);

        return baseMapper.convertToDTO(updated);
    }

    @Transactional
    public void deleteById(ID id) {
        baseRepository.deleteById(id);
    }
}
