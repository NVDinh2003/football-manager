package com.nvd.footballmanager.model.entity;

import com.nvd.footballmanager.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    private UUID id;

    @Column(length = 20)
    private String nickname;
    @Column(length = 25)
    private String position;
    private int shirtNumber;
    @Enumerated(EnumType.STRING)    // giá trị của enum sẽ được lưu dưới dạng string trong db
    @Column(length = 16)
    private Role role;
    private double fee;

    @ManyToMany
    @JoinTable(
            name = "member_match",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "match_id"))
    Set<Match> matches;


    @OneToMany(mappedBy = "member")
    private List<Comment> comments;

    //      USER - MEMBER - TEAM
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

}
