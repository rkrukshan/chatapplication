package com.chatapplication.chatapplication.service;

import com.chatapplication.chatapplication.dto.ChatMessage;
import com.chatapplication.chatapplication.dto.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final RedisMessageService redisMessageService;

    public ChatMessage processAndSendChatMessage(ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        log.info("Processing chat message from user: {}", chatMessage.getUserName());

        redisMessageService.publish(chatMessage);
        return chatMessage;
    }

    public ChatMessage addUserToChat(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getUserName());
        chatMessage.setMessageType(MessageType.JOIN);
        chatMessage.setMessage(chatMessage.getUserName() + " joined the chat");
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        log.info("User joined: {}", chatMessage.getUserName());

        redisMessageService.publish(chatMessage);
        return chatMessage;
    }

    public void handleUserDisconnect(String username) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageType(MessageType.LEAVE);
        chatMessage.setUserName(username);
        chatMessage.setMessage(username + " Left the Chat");
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        log.info("User disconnected: {}", username);
        redisMessageService.publish(chatMessage);
    }
}