package com.chatapplication.chatapplication.controller;

import com.chatapplication.chatapplication.dto.ChatMessage;
import com.chatapplication.chatapplication.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public ChatMessage sendChatMessage(@Payload ChatMessage chatMessage) {
        log.debug("Received chat message from user: {}", chatMessage.getUserName());
        return chatService.processAndSendChatMessage(chatMessage);
    }

    @MessageMapping("/chat.adduser")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        log.debug("Adding user to chat: {}", chatMessage.getUserName());
        return chatService.addUserToChat(chatMessage, headerAccessor);
    }
}