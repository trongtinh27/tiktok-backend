package com.tiktok.project.service;

import com.tiktok.project.entity.Message;

import java.util.List;

public interface MessageService {
    Message saveMessage(Message message);
    List<Message> getMessageBySenderAndReceiver(int senderId, int receiverId);
    List<Message> getMessageByChatRoom(int chatRoomId);
}
