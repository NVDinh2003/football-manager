package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends BaseRepository<Notification, UUID> {
    List<Notification> findByTeamId(UUID teamId);

    List<Notification> findAllByUserId(UUID userId);
}
