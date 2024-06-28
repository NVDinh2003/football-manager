package com.nvd.footballmanager.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NotiSendRequest {
    @NotNull
    UUID teamId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
