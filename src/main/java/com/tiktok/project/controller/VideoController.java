package com.tiktok.project.controller;

import com.tiktok.project.dto.VideoDTO;
import com.tiktok.project.entity.Video;
import com.tiktok.project.service.ImpService.VideoImpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/video")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VideoController {

    @Autowired
    private VideoImpService videoImpService;

    @GetMapping("/all")
    public ResponseEntity<List<VideoDTO>> getAllVideo() {
        List<Video> videoList = videoImpService.getAllVideo();
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (Video video: videoList) {
            videoDTOS.add(new VideoDTO(video.getId(),
                    video.getUser().getId(),
                    video.getUser().getUsername(),
                    video.getUser().getProfilePictureUrl(),
                    video.getThumbnailUrl(),
                    video.getVideoUrl(),
                    video.getDescription(),
                    video.getShape(),
                    video.getCommentsCount(),
                    video.getLikesCount(),
                    video.getCollectCount()
                    ));
        }

        return ResponseEntity.ok(videoDTOS);
    }
}
