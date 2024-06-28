package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends BaseRepository<Comment, UUID> {
}
