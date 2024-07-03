package com.nvd.footballmanager.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotiSendRequest {
    UUID teamId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
