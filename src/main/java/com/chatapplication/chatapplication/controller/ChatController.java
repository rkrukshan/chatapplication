package com.chatapplication.chatapplication.controller;

import com.chatapplication.chatapplication.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisTemplate redisTemplate;

    @MessageMapping("/chat.send")
    public ChatMessage sendChatMessage(@Payload ChatMessage chatMessage){
    chatMessage.setTimestamp(LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
    redisTemplate.convertAndSend("chat", chatMessage);
    }

}
