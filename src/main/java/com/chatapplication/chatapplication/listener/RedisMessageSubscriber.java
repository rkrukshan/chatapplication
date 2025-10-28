package com.chatapplication.chatapplication.listener;

import com.chatapplication.chatapplication.dto.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
    String publishedMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());

    try{
        ChatMessage chatMessage = objectMapper.readValue(publishedMessage, ChatMessage.class);
        simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
    } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
    }

    }
}
