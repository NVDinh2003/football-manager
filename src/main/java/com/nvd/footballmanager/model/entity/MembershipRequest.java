package com.nvd.footballmanager.model.entity;

import com.nvd.footballmanager.model.enums.MembershipRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "membership_request")
public class MembershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;


    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = false, columnDefinition = "enum('PENDING','ACCEPTED','REJECTED') NOT NULL DEFAULT 'PENDING'")
    private MembershipRequestStatus status;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant requestDate;

    //    @UpdateTimestamp
    private Instant responseDate = null;


    //      USER - MEMBERSHIP_REQUEST - TEAM
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @PrePersist
    protected void onCreate() {
        this.responseDate = null;
    }

    @PreUpdate
    protected void onUpdate() {
        this.responseDate = Instant.now();
    }
}
