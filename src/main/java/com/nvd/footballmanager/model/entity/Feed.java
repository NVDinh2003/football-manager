package com.nvd.footballmanager.model.entity;

import com.nvd.footballmanager.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "feeds")
public class Feed extends BaseModel {
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "feed")
    private List<Comment> comments;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_request_id", referencedColumnName = "id")
    private MatchRequest matchRequest;
}
