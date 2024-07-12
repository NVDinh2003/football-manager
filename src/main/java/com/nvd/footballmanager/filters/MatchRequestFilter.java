package com.nvd.footballmanager.filters;

import com.nvd.footballmanager.model.enums.MatchRequestStatus;
import com.nvd.footballmanager.model.enums.MatchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequestFilter extends BaseFilter {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String venue;
    private String locationDetails;
    private MatchType matchType;
    //    private String note;
    private MatchRequestStatus status;
    private UUID teamId;
    private int rankPoints;
    private int rankPointRange;
}
