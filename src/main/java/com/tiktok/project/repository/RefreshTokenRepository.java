package com.tiktok.project.repository;

import com.tiktok.project.entity.RefreshToken;
import com.tiktok.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(int userId);
    @Modifying
    void deleteByUser(User user);
}
