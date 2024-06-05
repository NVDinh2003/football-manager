package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.ApiResponse;
import com.nvd.footballmanager.dto.BaseDTO;
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
    public ResponseEntity<ApiResponse> findAll() {
        List<DTO> dtos = baseService.findAll();

        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable("id") ID id) {
        return ResponseEntity.ok(ApiResponse.success(baseService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody @Valid DTO dto) {
        return ResponseEntity.ok(ApiResponse.created(baseService.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") ID id,
            @RequestBody @Valid DTO dto
    ) {
        return ResponseEntity.ok(ApiResponse.success(baseService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") ID id) {
        baseService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
