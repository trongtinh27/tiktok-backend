package com.tiktok.project.dto.response;

import java.util.Date;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponse {
    private int id;
    private UserMessage sender;
    private UserMessage receiver;
    private Date created;
    private String content;


    @Data
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
     public static class UserMessage {
        int id;
        String username;
        String avatar;
    }
}
