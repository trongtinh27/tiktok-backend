package com.tiktok.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Tiền tố cho các request từ client gửi lên server
        registry.setApplicationDestinationPrefixes("/app");

        // Các topic dùng cho chat và bình luận realtime
        registry.enableSimpleBroker(
                "/topic/chat",      // Dùng cho chat nhóm
                "/queue/messages",
                "/chat.sendMessage",
                "/queue/chat",      // Dùng cho tin nhắn riêng tư
                "/topic/comments"   // Dùng cho bình luận realtime
        );

        // Dùng cho tin nhắn riêng tư
        registry.setUserDestinationPrefix("/user");
    }
}
