package com.nvd.footballmanager.service.firebase;

import com.google.firebase.messaging.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nvd.footballmanager.dto.notification.NotiSendRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FCMService {
    @Value("${app.base.url}")
    private String baseUrl;
    private Logger logger = LoggerFactory.getLogger(FCMService.class);

    public void sendMessageToToken(NotiSendRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToToken(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response + " msg " + jsonOutput);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private Message getPreconfiguredMessageToToken(NotiSendRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(NotiSendRequest request) {
        WebpushConfig webpushConfig = getWebpushConfig(request.getTopic());
        Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getContent())
                .build();
        return Message.builder()
                .setWebpushConfig(webpushConfig).setNotification(notification);
    }

    private WebpushConfig getWebpushConfig(String topic) {
        return WebpushConfig.builder()
                .setNotification(new WebpushNotification(topic, null))
                .setFcmOptions(WebpushFcmOptions.withLink(baseUrl))
                .build();
    }
}