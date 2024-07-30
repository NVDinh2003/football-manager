package com.nvd.footballmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
// enable WebSocket với STOMP
@EnableWebSocketMessageBroker       // interface cung cấp các method cấu hình WebSocket với STOMP
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final String[] URL_ACCEPT = {
            "http://localhost:8080"
    };


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // ... gì đó chuyển tiếp các messages đến client, clients subscribe đế nhận message
        registry.enableSimpleBroker("/topic");
        // prefix cho các message gửi từ client đến server
        registry.setApplicationDestinationPrefixes("/ws");
        // message gửi tới user cụ thê
        registry.setUserDestinationPrefix("/user");
    }

    @Override   // register các endpoint cho WebSocket
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // register 1 endpoint WebSocket tại /stomp, nơi các client sẽ bắt đầu phiên socket
        registry.addEndpoint("/stomp")
                .setAllowedOriginPatterns(URL_ACCEPT)
                .withSockJS();  // support old browser
    }

    @Override   // kiem soat payload limit...
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(64 * 1024); // 64KB
        registration.setSendBufferSizeLimit(512 * 1024); // 512KB
        registration.setSendTimeLimit(20 * 1000); // 20 seconds
    }
}
