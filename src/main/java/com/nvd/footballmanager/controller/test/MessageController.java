package com.nvd.footballmanager.controller.test;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MessageController {


    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Map<String, String> getMessage(final String message) throws InterruptedException {
        Thread.sleep(1000);
        Map<String, String> response = new HashMap<>();
        response.put("content", HtmlUtils.htmlEscape(message));
        return response;
    }

    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public Map<String, String> getPrivateMessage(final String message, final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        Map<String, String> response = new HashMap<>();
        response.put("content", HtmlUtils.htmlEscape("Private message to user " + principal.getName() + ": " + message));
        return response;
    }
}
