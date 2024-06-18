package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.BaseDTO;
import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.model.BaseModel;
import com.nvd.footballmanager.service.BaseService;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@MappedSuperclass
@Validated
public abstract class BaseController<E extends BaseModel,
        DTO extends BaseDTO<ID>,
        ID extends UUID> {

    private final BaseService<E, DTO, ID> baseService;

    protected BaseController(BaseService<E, DTO, ID> baseService) {
        this.baseService = baseService;
    }

    @GetMapping
    public ResponseEntity<CustomApiResponse> findAll() {
        List<DTO> dtos = baseService.findAll();

        return ResponseEntity.ok(CustomApiResponse.success(dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse> findById(@PathVariable("id") ID id) {
        return ResponseEntity.ok(CustomApiResponse.success(baseService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CustomApiResponse> create(@RequestBody @Valid DTO dto) {
        return ResponseEntity.ok(CustomApiResponse.created(baseService.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse> update(
            @PathVariable("id") ID id,
            @RequestBody @Valid DTO dto
    ) {
        return ResponseEntity.ok(CustomApiResponse.success(baseService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse> delete(@PathVariable("id") ID id) {
        baseService.deleteById(id);
        return ResponseEntity.ok(CustomApiResponse.noContent());
    }
}
