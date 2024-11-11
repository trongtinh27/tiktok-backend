package com.tiktok.project.service.ImpService;

import com.tiktok.project.entity.ChatRoom;
import com.tiktok.project.entity.User;
import com.tiktok.project.repository.ChatRoomRepository;
import com.tiktok.project.service.ChatRoomService;
import com.tiktok.project.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatRoomImpService implements ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserService userService;


    @Override
    public ChatRoom findOrCreateChatRoom(int userOneId, int userTwoId) {
        User userOne = userService.findUserById(userOneId);
        User userTwo = userService.findUserById(userTwoId);

        // Tìm phòng chat theo cả hai cách (userOne, userTwo) và (userTwo, userOne)
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByUserOneAndUserTwo(userOne, userTwo);
        if (chatRoomOptional.isPresent()) {
            return chatRoomOptional.get(); // Trả về phòng chat đã tồn tại
        }

        // Kiểm tra theo thứ tự ngược lại
        chatRoomOptional = chatRoomRepository.findByUserOneAndUserTwo(userTwo, userOne);
        if (chatRoomOptional.isPresent()) {
            return chatRoomOptional.get(); // Trả về phòng chat đã tồn tại
        }

        // Nếu chưa có, tạo phòng chat mới
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUserOne(userOne);
        chatRoom.setUserTwo(userTwo);
        return chatRoomRepository.save(chatRoom); // Lưu vào cơ sở dữ liệu
    }

}
