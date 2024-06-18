package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.Team;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamRepository extends BaseRepository<Team, UUID> {

}
