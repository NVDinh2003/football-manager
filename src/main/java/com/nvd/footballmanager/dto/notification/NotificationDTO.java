package com.nvd.footballmanager.dto.notification;

import com.nvd.footballmanager.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO extends BaseDTO<UUID> {
    private String title;
    private String content;
    private String sender;
    private UUID teamId;
}
