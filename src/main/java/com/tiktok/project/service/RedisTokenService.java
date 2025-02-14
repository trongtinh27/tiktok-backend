package com.tiktok.project.service;

import com.tiktok.project.entity.RedisToken;
import com.tiktok.project.exception.InvalidDataException;
import com.tiktok.project.repository.RedisTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTokenService {
    private final RedisTokenRepository redisTokenRepository;

    public void save(RedisToken token) {
        redisTokenRepository.save(token);
    }

    public void remove(String id) {
        if(isExits(id)) redisTokenRepository.deleteById(id);
    }

    public boolean isExits(String id) {
        return redisTokenRepository.existsById(id);
    }
}
