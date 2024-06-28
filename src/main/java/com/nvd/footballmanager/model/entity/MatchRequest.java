package com.nvd.footballmanager.model.entity;

import com.nvd.footballmanager.model.enums.MatchRequestStatus;
import com.nvd.footballmanager.model.enums.MatchType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "match_requests")
public class MatchRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    private LocalDateTime time;
    private String venue;
    private String locationDetails;
    @Enumerated(EnumType.STRING)
    @Column(length = 15, columnDefinition = "enum('FIVE_A_SIDE','SEVEN_A_SIDE','ELEVEN_A_SIDE')")
    private MatchType matchType;
    @Column(columnDefinition = "TEXT")
    private String note;
    @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = false, columnDefinition = "enum('NEW','DONE') DEFAULT 'NEW'")
    private MatchRequestStatus status;

    @OneToOne(mappedBy = "matchRequest")
    private Feed feed;
}
