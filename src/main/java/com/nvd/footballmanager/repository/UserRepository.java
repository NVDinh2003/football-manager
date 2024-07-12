package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.BaseFilter;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, BaseFilter, UUID> {

    @Override
    @Query("""
            SELECT u FROM User u
            WHERE ( :#{#filter.s == null || #filter.s.isEmpty()} = TRUE
                OR LOWER(u.username) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
                OR LOWER(u.name) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
                OR LOWER(u.email) = LOWER(:#{#filter.s})
                OR LOWER(u.phoneNumber) = LOWER(:#{#filter.s})
            )
            """)
    Page<User> findAllWithFilter(Pageable pageable, BaseFilter filter);


    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByRole(UserRole role);
}
