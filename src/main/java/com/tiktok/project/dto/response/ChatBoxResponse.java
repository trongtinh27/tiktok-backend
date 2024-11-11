package com.tiktok.project.dto.response;

import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatBoxResponse {
    private int chatRoom;
    private UserChatBox receiver;
    private List<ChatMessageResponse> listMessage;

    @Data
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserChatBox {
        private int id;
        private String username;
        private String fullname;
        private String avatar;
    }


}
