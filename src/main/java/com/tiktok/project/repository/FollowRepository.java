package com.tiktok.project.repository;

import com.tiktok.project.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follower, Integer> {
    boolean existsByFollowerIdAndFollowedId(int followerId, int followedId);
}
