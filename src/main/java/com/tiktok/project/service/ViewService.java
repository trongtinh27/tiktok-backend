package com.tiktok.project.service;

import com.tiktok.project.entity.User;
import com.tiktok.project.entity.Video;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewService {
    void saveView(Authentication authentication, int video);
}
