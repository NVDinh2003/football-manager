package com.nvd.footballmanager.repository;

import com.nvd.footballmanager.filters.MemberFilter;
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.model.enums.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface MemberRepository extends BaseRepository<Member, MemberFilter, UUID> {

    @Override
    @Query("""
            SELECT m FROM Member m 
            WHERE ( :#{#filter.s == null || #filter.s.isEmpty()} = TRUE
                OR LOWER(m.nickname) LIKE LOWER(CONCAT('%', :#{#filter.s}, '%'))
            )
            AND (:#{#filter.teamId == NULL} = TRUE OR m.team.id = :#{#filter.teamId}) 
            AND (:#{#filter.position == NULL} = TRUE OR m.position = :#{#filter.position}) 
            AND (:#{#filter.role == NULL} = TRUE OR m.role >= :#{#filter.role})
            """)
    Page<Member> findAllWithFilter(Pageable pageable, MemberFilter filter);


    Optional<Member> findByUserIdAndTeamId(UUID userId, UUID teamId);

    Optional<Member> findByIdAndTeamId(UUID id, UUID teamId);

    List<Member> findAllByTeamId(UUID teamId);

    Member findByRoleAndTeamId(MemberRole memberRole, UUID teamId);

    Optional<Member> findByRoleAndUserId(MemberRole memberRole, UUID userId);

    Optional<Member> findByIdAndUserId(UUID memberId, UUID id);

    long countByUserId(UUID userId);
}
