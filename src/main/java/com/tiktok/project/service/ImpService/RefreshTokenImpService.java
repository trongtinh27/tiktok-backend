package com.tiktok.project.service.ImpService;

import com.tiktok.project.entity.RedisToken;
import com.tiktok.project.entity.RefreshToken;
import com.tiktok.project.repository.RefreshTokenRepository;
import com.tiktok.project.service.JwtService;
import com.tiktok.project.service.RedisTokenService;
import com.tiktok.project.service.RefreshTokenService;
import com.tiktok.project.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefreshTokenImpService implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userService.findUserByUsername(username))
                .token(jwtService.generateRefreshToken(username))
                .expiryDate(Instant.now().plusMillis(JwtService.EXPIRATIONTIME_REFRESHTOKEN))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public Optional<RefreshToken> findByUserId(int userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now())< 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signing request");
        }
        return token;
    }

    @Override
    public void deleteByUserId(int userId) {
        refreshTokenRepository.deleteByUser(userService.findUserById(userId));

    }

    @Override
    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
