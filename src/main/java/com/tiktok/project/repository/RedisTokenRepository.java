package com.tiktok.project.repository;

import com.tiktok.project.entity.RedisToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {
}
