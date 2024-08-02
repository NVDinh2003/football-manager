package com.nvd.footballmanager.dto.notification;

import com.nvd.footballmanager.dto.BaseDTO;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String sender;
    private UUID teamId;
}
