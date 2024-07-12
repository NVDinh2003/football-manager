package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.CommentFilter;
import com.nvd.footballmanager.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends BaseRepository<Comment, CommentFilter, UUID> {
    @Override
    @Query("""
            SELECT c FROM Comment c 
            WHERE (:#{#filter.s == null} = true OR LOWER(c.content) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%')))
            AND (:#{#filter.feedId == null} = true OR c.feed.id = :#{#filter.feedId})
            """)
    Page<Comment> findAllWithFilter(Pageable pageable, CommentFilter filter);
}
