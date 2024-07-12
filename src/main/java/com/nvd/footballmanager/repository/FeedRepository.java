package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.FeedFilter;
import com.nvd.footballmanager.model.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeedRepository extends BaseRepository<Feed, FeedFilter, UUID> {
    @Override
    @Query("""
            SELECT f FROM Feed f 
            WHERE ( :#{#filter.s == null || #filter.s.isEmpty()} = TRUE
                OR LOWER(f.title) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
                OR LOWER(f.content) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
            )
            AND (:#{#filter.userId == NULL} = TRUE OR f.user.id = :#{#filter.userId}) 
            """)
    Page<Feed> findAllWithFilter(Pageable pageable, FeedFilter filter);
}
