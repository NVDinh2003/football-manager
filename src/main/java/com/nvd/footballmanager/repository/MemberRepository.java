package com.nvd.footballmanager.repository;

<<<<<<< Updated upstream
public interface MemberRepository {
=======
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.enums.MemberRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends BaseRepository<Member, UUID> {
    Optional<Member> findByUserIdAndTeamId(UUID userId, UUID teamId);

    Optional<Member> findByIdAndTeamId(UUID id, UUID team_id);

    List<Member> findAllByTeamId(UUID teamId);

    Member findByRoleAndTeamId(MemberRole memberRole, UUID teamId);
>>>>>>> Stashed changes
}
