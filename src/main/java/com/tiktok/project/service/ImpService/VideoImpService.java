package com.tiktok.project.service.ImpService;

import com.tiktok.project.dto.request.UploadVideoRequest;
import com.tiktok.project.entity.User;
import com.tiktok.project.entity.Video;
import com.tiktok.project.repository.VideoRepository;
import com.tiktok.project.service.UserService;
import com.tiktok.project.service.VideoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VideoImpService implements VideoService {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private UserService userService;
    @Override
    public List<Video> getAllVideo() {
        return videoRepository.findAll();
    }

    @Override
    public void addVideo(UploadVideoRequest videoForm) {
        Video video = new Video();
        User user = userService.findUserById(videoForm.getUserId());
        video.setUser(user);
        video.setVideoUrl(videoForm.getVideoUrl());
        video.setThumbnailUrl(videoForm.getThumbnailUrl());
        video.setDescription(videoForm.getDescription());
        video.setShape(videoForm.getShape());
        video.setCreatedAt(new Date());

        videoRepository.save(video);

    }

}
