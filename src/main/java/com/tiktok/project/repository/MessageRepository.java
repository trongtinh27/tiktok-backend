package com.tiktok.project.repository;

import com.tiktok.project.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderIdAndReceiverId(int senderId, int receiverId);
    List<Message> findAllByChatRoomId(int chatRoomId);
}
