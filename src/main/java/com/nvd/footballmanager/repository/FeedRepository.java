package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.Feed;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeedRepository extends BaseRepository<Feed, UUID> {
}
