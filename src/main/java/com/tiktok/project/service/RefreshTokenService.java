package com.tiktok.project.service;

import com.tiktok.project.entity.RefreshToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username);
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(int userId);
    RefreshToken verifyExpiration(RefreshToken token);
    void deleteByUserId(int userId);
    void delete(RefreshToken refreshToken);
}
