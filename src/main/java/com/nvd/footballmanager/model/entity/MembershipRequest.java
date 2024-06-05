package com.nvd.footballmanager.model.entity;

import com.nvd.footballmanager.model.enums.MembershipRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "membership_request")
public class MembershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, length = 36)
    private UUID id;


    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = false)
    private MembershipRequestStatus status = MembershipRequestStatus.PENDING;
    private LocalDate requestDate;
    private LocalDate responseDate;


    //      USER - MEMBERSHIP_REQUEST - TEAM
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

}
