package com.nvd.footballmanager.model.entity;

import com.nvd.footballmanager.model.enums.EquipmentCondition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "equipments")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    private UUID id;
    private String name;
    private int quantity = 0;
    @Enumerated(EnumType.STRING)
    @Column(length = 6, nullable = false)
    private EquipmentCondition condition = EquipmentCondition.NEW;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
}
