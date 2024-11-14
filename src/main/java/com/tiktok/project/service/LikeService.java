package com.tiktok.project.service;

import com.tiktok.project.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeService {
    boolean likeVideo(User user, int videoId);
    int getCountLikeVideo(int videoId);
    boolean isVideoLiked(User user, int videoId);
}
