package com.nvd.footballmanager.dto;

import com.nvd.footballmanager.model.enums.MatchRequestStatus;
import com.nvd.footballmanager.model.enums.MatchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchRequestDTO {
    private LocalDateTime time;
    private String venue;
    private String locationDetails;
    private MatchType matchType;
    private String note;
    private MatchRequestStatus status;

//    private Feed feed;
}
