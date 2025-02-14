package com.tiktok.project.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@RedisHash("RedisToken")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisToken implements Serializable {
    private String id;
    private String refreshToken;
    @TimeToLive // TTL tính bằng giây
    private Long expiration; // Số giây token sẽ tồn tại
}