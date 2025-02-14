package com.tiktok.project.service;

import com.tiktok.project.dto.request.CollectRequest;
import com.tiktok.project.dto.request.LikeRequest;
import com.tiktok.project.dto.request.UploadVideoRequest;
import com.tiktok.project.dto.response.CollectResponse;
import com.tiktok.project.dto.response.DetailVideoDTO;
import com.tiktok.project.dto.response.LikeResponse;
import com.tiktok.project.dto.response.VideoDTO;
import com.tiktok.project.entity.User;
import com.tiktok.project.entity.Video;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface VideoService {
    List<Video> getAllVideo();
    void addVideo(UploadVideoRequest videoForm);
    List<VideoDTO> getVideoByUser(String username);
    DetailVideoDTO getVideoByUsernameAndVideoId(int userId, String username, int videoId);
    List<VideoDTO> getVideoFeed(Authentication authentication, int pageNo, int pageSize);
    LikeResponse toggleLikeVideo(LikeRequest likeRequest);
    CollectResponse toggleCollectVideo(CollectRequest collectRequest);
}
