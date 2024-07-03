package com.nvd.footballmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchDTO extends BaseDTO<UUID> {
    private UUID team1Id;
    private int team1Scored = 0;
    private UUID team2Id;
    private int team2Scored = 0;
    private Instant time;
    private String venue;
}
