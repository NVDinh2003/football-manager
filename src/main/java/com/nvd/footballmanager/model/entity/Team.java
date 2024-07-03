package com.nvd.footballmanager.model.entity;

import com.nvd.footballmanager.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "teams")
public class Team extends BaseModel {

    private String name;
    private String logo;
    private String address;
    @Column(columnDefinition = "int default 0")
    private int rankPoints = 0;

    //  TEAM - MATCH
    @OneToMany(mappedBy = "team1", fetch = FetchType.LAZY) // chỉ tải khi được truy cập
    private Set<Match> team1;

    @OneToMany(mappedBy = "team2", fetch = FetchType.LAZY)
    private Set<Match> team2;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Member> members;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Achievement> achievements;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MembershipRequest> membershipRequests;

}
