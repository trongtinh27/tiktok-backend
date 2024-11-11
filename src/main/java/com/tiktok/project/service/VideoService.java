package com.tiktok.project.service;

import com.tiktok.project.dto.request.UploadVideoRequest;
import com.tiktok.project.entity.Video;

import java.util.List;

public interface VideoService {
    List<Video> getAllVideo();
    void addVideo(UploadVideoRequest videoForm);
}
