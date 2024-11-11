package com.tiktok.project.repository;

import com.tiktok.project.entity.ChatRoom;
import com.tiktok.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    Optional<ChatRoom> findByUserOneAndUserTwo(User userOne, User UserTwo);
}
