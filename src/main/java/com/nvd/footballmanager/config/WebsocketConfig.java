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
        // ...something that forwards messages to the client, and clients subscribe to receive message
        registry.enableSimpleBroker("/topic");
        // prefix for messages sent from client to server
        registry.setApplicationDestinationPrefixes("/ws");
        // message sent to a specific user
        registry.setUserDestinationPrefix("/user");
    }

    @Override   // register endpoints cho WebSocket
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register a WebSocket endpoint at /stomp, where clients will initiate the socket session
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
