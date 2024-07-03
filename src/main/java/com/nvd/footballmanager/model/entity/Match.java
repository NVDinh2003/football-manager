package com.nvd.footballmanager.model.entity;

import com.nvd.footballmanager.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "matches")
public class Match extends BaseModel {

    @Column(name = "team1_scored", columnDefinition = "NOT NULL DEFAULT 0")
    private int team1Scored = 0;
    @Column(name = "team2_scored", columnDefinition = "NOT NULL DEFAULT 0")
    private int team2Scored = 0;
    private Instant time;
    private String venue;

    @ManyToMany(mappedBy = "matches")
    Set<Member> members;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team1_id", referencedColumnName = "id")
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team2_id", referencedColumnName = "id")
    private Team team2;
}
