package com.nvd.footballmanager.dto;

import com.nvd.footballmanager.model.enums.MembershipRequestStatus;
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
public class MembershipRequestDTO {
    private UUID id;
    private MembershipRequestStatus status;
    private Instant requestDate;
    private Instant responseDate;
    private UUID userId;
    private UUID teamId;
}
