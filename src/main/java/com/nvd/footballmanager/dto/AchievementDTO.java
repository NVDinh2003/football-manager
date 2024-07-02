package com.nvd.footballmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AchievementDTO {
    private UUID id;
    private String title;
    private String description;
    private LocalDate date;
    private TeamDTO team;
}
