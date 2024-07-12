package com.nvd.footballmanager.dto;

import com.nvd.footballmanager.model.enums.MatchRequestStatus;
import com.nvd.footballmanager.model.enums.MatchType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchRequestDTO {
    private UUID id;
    private LocalDateTime time;
    private String venue;
    private String locationDetails;
    private MatchType matchType;
    private String note;
    private MatchRequestStatus status;
    @NotNull
    private UUID teamId;

//    private Feed feed;
}
