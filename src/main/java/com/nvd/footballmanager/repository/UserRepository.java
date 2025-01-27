package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {

    Optional<User> findByUsername(String username);
}
