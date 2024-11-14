package com.tiktok.project.service;

import com.tiktok.project.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectService {
    boolean collectVideo(User user, int videoId);
    int getCountCollectVideo(int videoId);
    boolean isVideoCollected(User user, int videoId);
}
