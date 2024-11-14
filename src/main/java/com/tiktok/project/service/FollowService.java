package com.tiktok.project.service;

import com.tiktok.project.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FollowService {
    boolean checkFollowerWithFollowed(int followerId, int followedId);
    List<User> getMutualFollowings(User user);

    boolean toggleFollow(User follower, User followed );


}
