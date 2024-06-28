package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.entity.MemberNotification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemberNotificationRepository extends BaseRepository<MemberNotification, UUID> {
    List<MemberNotification> findByMemberAndNotificationIdIn(Member currentMember, List<UUID> notifyIds);
}