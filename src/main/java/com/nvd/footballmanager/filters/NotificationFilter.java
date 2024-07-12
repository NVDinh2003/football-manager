package com.nvd.footballmanager.filters;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NotificationFilter extends BaseFilter {
    private UUID userId;
    private UUID memberId;
    private UUID teamId;
}
