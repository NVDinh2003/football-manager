package com.nvd.footballmanager.controller.test;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class MessageDTO {
    private UUID memberId;
    private String message;
    private String sender;

    public MessageDTO(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }
}

