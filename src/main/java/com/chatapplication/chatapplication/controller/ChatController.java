package com.chatapplication.chatapplication.controller;

import com.chatapplication.chatapplication.dto.ChatMessage;
import com.chatapplication.chatapplication.dto.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final RedisTemplate redisTemplate;

    @MessageMapping("/chat.send")
    public ChatMessage sendChatMessage(@Payload ChatMessage chatMessage){
    chatMessage.setTimestamp(LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
    redisTemplate.convertAndSend("chat", chatMessage);
    return chatMessage;
    }

    @MessageMapping("/chat.adduser")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor)
    {
        headerAccessor.getSessionAttributes().put("Username", chatMessage.getUserName());
        chatMessage.setMessageType(MessageType.JOIN);
        chatMessage.setMessage(chatMessage.getUserName() + " Joined the Chat");
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
        log.info("User Joined: {}", chatMessage.getUserName());

        redisTemplate.convertAndSend("chat", chatMessage);
        return chatMessage;
    }

}
