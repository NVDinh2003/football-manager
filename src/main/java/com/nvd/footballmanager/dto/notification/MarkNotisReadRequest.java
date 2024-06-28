package com.nvd.footballmanager.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkNotisReadRequest {
    private UUID teamId;
    private List<UUID> notifyIds;
}
