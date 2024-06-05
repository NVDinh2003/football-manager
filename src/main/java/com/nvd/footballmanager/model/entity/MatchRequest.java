package com.nvd.footballmanager.model.entity;

import com.nvd.footballmanager.model.BaseModel;
import com.nvd.footballmanager.model.enums.MatchRequestStatus;
import com.nvd.footballmanager.model.enums.MatchType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "match_request")
public class MatchRequest extends BaseModel {

    private LocalDateTime time;
    private String venue;
    private String locationDetails;
    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private MatchType matchType;
    @Column(columnDefinition = "TEXT")
    private String note;
    @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = false)
    private MatchRequestStatus status = MatchRequestStatus.NEW;

    @OneToOne(mappedBy = "matchRequest")
    private Feed feed;
}
