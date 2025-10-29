package com.chatapplication.chatapplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @NotBlank(message = "Message content cannot be empty")
    private String message;

    private String timestamp;

    @NotBlank(message = "Username cannot be empty")
    private String userName;

    @NotNull(message = "Message type cannot be null")
    private MessageType messageType;
}