package com.tiktok.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ChatMessage {
    @NotNull(message = "senderId must be not null")
    private int senderId;
    @NotNull(message = "receiverId must be not null")
    private int receiverId;
    @NotNull(message = "content must be not null")
    private String content;

}
