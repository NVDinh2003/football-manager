package com.nvd.footballmanager.dto.notification;

import com.nvd.footballmanager.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO extends BaseDTO<UUID> implements Serializable {
    private String title;
    private String content;
    private String sender;
    private UUID teamId;

    private String topic;
    private String token;
}
