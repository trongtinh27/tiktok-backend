package com.tiktok.project.service.ImpService;

import com.tiktok.project.entity.Message;
import com.tiktok.project.repository.MessageRepository;
import com.tiktok.project.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MessageImpService implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessageBySenderAndReceiver(int senderId, int receiverId) {
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
    }

    @Override
    public List<Message> getMessageByChatRoom(int chatRoomId) {
        return messageRepository.findAllByChatRoomId(chatRoomId);
    }

}
