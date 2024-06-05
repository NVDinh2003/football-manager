package com.nvd.footballmanager.model.entity;

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
@Table(name = "achievements")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    private UUID id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
}
