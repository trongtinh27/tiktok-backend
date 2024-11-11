package com.tiktok.project.controller;

import com.tiktok.project.dto.ChatMessage;
import com.tiktok.project.dto.response.ChatBoxResponse;
import com.tiktok.project.dto.response.ChatMessageResponse;
import com.tiktok.project.entity.ChatRoom;
import com.tiktok.project.entity.Message;
import com.tiktok.project.entity.User;
import com.tiktok.project.service.ChatRoomService;
import com.tiktok.project.service.MessageService;
import com.tiktok.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping("/create-chat-room")
    public ResponseEntity<?> createChatRoom(@RequestParam int userOneId, @RequestParam int userTwoId) {
        return ResponseEntity.ok(chatRoomService.findOrCreateChatRoom(userOneId, userTwoId).getId());
    }

    @MessageMapping("/chat.sendMessage/{roomId}")
    @SendTo("/queue/messages/{roomId}") // Sử dụng roomId trong đích gửi tin nhắn
    public ChatMessageResponse sendMessage(@DestinationVariable String roomId, ChatMessage chatMessage) {
        User sender = userService.findUserById(chatMessage.getSenderId());
        User receiver = userService.findUserById(chatMessage.getReceiverId());

        ChatRoom chatRoom = chatRoomService.findOrCreateChatRoom(sender.getId(), receiver.getId());

        // Lưu tin nhắn
        Message message = new Message();
        message.setContent(chatMessage.getContent());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setChatRoom(chatRoom);
        message = messageService.saveMessage(message);

        // Tạo response
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId());
        response.setSender(new ChatMessageResponse.UserMessage(sender.getId(), sender.getUsername(), sender.getProfilePictureUrl()));
        response.setReceiver(new ChatMessageResponse.UserMessage(receiver.getId(), sender.getUsername(), receiver.getProfilePictureUrl()));
        response.setCreated(message.getCreatedAt());
        response.setContent(message.getContent());
        return response;
    }


    @MessageMapping("/chat.addUser/{roomId}")
    @SendTo("/topic/chat-room/{roomId}")
    public ChatMessage addUser(@DestinationVariable String roomId, ChatMessage chatMessage, SimpMessageHeaderAccessor accessor) {
        accessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        accessor.getSessionAttributes().put("roomId", roomId);
        return chatMessage;
    }

    @GetMapping("/get/{senderId}/{receiverId}")
    public List<Message> getMessagesBetweenUsers(@PathVariable int senderId, @PathVariable int receiverId) {
        return messageService.getMessageBySenderAndReceiver(senderId, receiverId);
    }

    @GetMapping("/get-chat")
    public ResponseEntity<?> getChatBox(@RequestParam int senderId,
                                        @RequestParam int receiverId,
                                        @RequestParam int offset,
                                        @RequestParam int limit) {
        User receiver = userService.findUserById(receiverId);
        ChatRoom chatRoom = chatRoomService.findOrCreateChatRoom(senderId, receiverId);

        if(receiver != null) {
            List<Message> messageList = messageService.getMessageByChatRoom(chatRoom.getId());
//            Collections.reverse(messageList);
//            messageList.stream()
//                    .skip(offset)
//                    .limit(limit)
//                    .toList();

            ChatBoxResponse chatBoxResponse = new ChatBoxResponse();

            chatBoxResponse.setReceiver(new ChatBoxResponse.UserChatBox(
                    receiver.getId(),
                    receiver.getUsername(),
                    receiver.getDisplayName(),
                    receiver.getProfilePictureUrl()
            ));
            List<ChatMessageResponse> chatMessageResponseList = new ArrayList<>();
            for (Message mess : messageList) {
                chatMessageResponseList.add(new ChatMessageResponse(
                        mess.getId(),
                        new ChatMessageResponse.UserMessage(
                                mess.getSender().getId(),
                                mess.getSender().getUsername(),
                                mess.getSender().getProfilePictureUrl()
                        ),
                        new ChatMessageResponse.UserMessage(
                                mess.getReceiver().getId(),
                                mess.getReceiver().getUsername(),
                                mess.getReceiver().getProfilePictureUrl()
                        ),
                        mess.getCreatedAt(),
                        mess.getContent()

                ));
            }
            chatBoxResponse.setListMessage(chatMessageResponseList);

            return ResponseEntity.ok(chatBoxResponse);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }
}
