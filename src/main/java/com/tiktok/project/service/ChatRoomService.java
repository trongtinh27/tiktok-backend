package com.tiktok.project.service;

import com.tiktok.project.entity.ChatRoom;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface ChatRoomService {
    ChatRoom findOrCreateChatRoom(int userOneId, int userTwoId);
}
