package com.tiktok.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private int senderId;
    private int receiverId;
    private String content;

}
