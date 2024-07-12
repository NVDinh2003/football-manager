package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.BaseFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean // không tạo bean trong context spring (kiểu 'template' chứ không phải là bean để spring quản lý)
// JpaSpecificationExecutor<E>: Interface cho phép thực thi truy vấn JPA criteria dựa trên các Specification.
// Điều này hữu ích khi bạn cần thực hiện các truy vấn phức tạp hơn mà không sử dụng trực tiếp truy vấn SQL hoặc JPQL.
public interface BaseRepository<E, FT extends BaseFilter, ID extends UUID> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {
//    Page<E> findAllWithFilter(Pageable pageable, FT filter);

    default Page<E> findAllWithFilter(Pageable pageable, FT filter) {
        throw new UnsupportedOperationException("findAllWithFilter is not implemented for this repository");
    }
}

