package com.tiktok.project.service;

import com.tiktok.project.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageService {
    Message saveMessage(Message message);
    List<Message> getMessageBySenderAndReceiver(int senderId, int receiverId);
    List<Message> getMessageByChatRoom(int chatRoomId);
}
