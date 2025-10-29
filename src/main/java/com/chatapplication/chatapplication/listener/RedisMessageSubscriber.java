package com.chatapplication.chatapplication.listener;

import com.chatapplication.chatapplication.dto.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private static final String PUBLIC_TOPIC = "/topic/public";

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishedMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());

            if (publishedMessage != null) {
                ChatMessage chatMessage = objectMapper.readValue(publishedMessage, ChatMessage.class);
                log.debug("Received message from Redis for user: {}", chatMessage.getUserName());

                simpMessageSendingOperations.convertAndSend(PUBLIC_TOPIC, chatMessage);
            } else {
                log.warn("Received null message from Redis");
            }

        } catch (JsonProcessingException e) {
            log.error("Error processing JSON message from Redis: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error in Redis message subscriber: {}", e.getMessage(), e);
        }
    }
}