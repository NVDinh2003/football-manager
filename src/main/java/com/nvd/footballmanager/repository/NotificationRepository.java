package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.NotificationFilter;
import com.nvd.footballmanager.model.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends BaseRepository<Notification, NotificationFilter, UUID> {

    @Override
    @Query("""
            SELECT n FROM Notification n
            WHERE (:#{#filter.teamId == NULL} = TRUE OR n.team.id = :#{#filter.teamId})
                  AND (:#{#filter.userId == NULL} = TRUE OR n.user.id = :#{#filter.userId})       
            """)
    Page<Notification> findAllWithFilter(Pageable pageable, NotificationFilter filter);
}
