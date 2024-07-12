package com.nvd.footballmanager.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchFilter extends BaseFilter {
    private UUID teamId;
    private Instant fromDate;
    private Instant toDate;
    private String venue;
}
