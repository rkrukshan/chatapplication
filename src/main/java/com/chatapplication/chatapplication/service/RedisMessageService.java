package com.chatapplication.chatapplication.service;

import com.chatapplication.chatapplication.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisMessageService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CHAT_CHANNEL = "chat";

    public void publish(ChatMessage chatMessage) {
        try {
            redisTemplate.convertAndSend(CHAT_CHANNEL, chatMessage);
            log.debug("Message published to Redis channel '{}' from user: {}", CHAT_CHANNEL, chatMessage.getUserName());
        } catch (Exception e) {
            log.error("Error publishing message to Redis: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to publish message to Redis", e);
        }
    }
}