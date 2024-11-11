package com.tiktok.project.repository;

import com.tiktok.project.entity.Follower;
import com.tiktok.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follower, Integer> {
    boolean existsByFollowerIdAndFollowedId(int followerId, int followedId);
}
