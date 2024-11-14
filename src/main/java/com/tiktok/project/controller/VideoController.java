package com.tiktok.project.controller;

import com.tiktok.project.dto.VideoDTO;
import com.tiktok.project.dto.request.CollectRequest;
import com.tiktok.project.dto.request.LikeRequest;
import com.tiktok.project.dto.request.UploadVideoRequest;
import com.tiktok.project.dto.response.CollectResponse;
import com.tiktok.project.dto.response.LikeResponse;
import com.tiktok.project.entity.User;
import com.tiktok.project.entity.Video;
import com.tiktok.project.service.*;
import com.tiktok.project.service.ImpService.VideoImpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/video")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VideoController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private FollowService followService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private UserService userService;

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<VideoDTO>> getAllVideo(@PathVariable int userId ) {
        List<Video> videoList = videoService.getAllVideo();
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
                    video.getCollectCount(),
                    followService.checkFollowerWithFollowed(userId,video.getUser().getId()),
                    likeService.isVideoLiked(userService.findUserById(userId), video.getId()),
                    collectService.isVideoCollected(userService.findUserById(userId), video.getId())
                    ));
        }

        return ResponseEntity.ok(videoDTOS);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(@RequestBody UploadVideoRequest videoRequest) {
        if(videoRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        videoService.addVideo(videoRequest);
        return ResponseEntity.ok("Upload Video successfully");
    }

    @PostMapping("/like")
    public ResponseEntity<?> toggleLikeVideo(@RequestBody LikeRequest likeRequest) {
        User user = userService.findUserById(likeRequest.getUserId());
        if(user != null) {
            LikeResponse likeResponse = new LikeResponse();
            likeResponse.setLikeStatus(likeService.likeVideo(user, likeRequest.getVideoId()));
            likeResponse.setCountLike(likeService.getCountLikeVideo(likeRequest.getVideoId()));
            return ResponseEntity.ok(likeResponse);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/collect")
    public ResponseEntity<?> toggleCollectVideo(@RequestBody CollectRequest collectRequest) {
        User user = userService.findUserById(collectRequest.getUserId());
        if(user != null) {
            CollectResponse collectResponse = new CollectResponse();
            collectResponse.setCollectStatus(collectService.collectVideo(user, collectRequest.getVideoId()));
            collectResponse.setCountCollect(collectService.getCountCollectVideo(collectRequest.getVideoId()));
            return ResponseEntity.ok((collectResponse));
        }
        return ResponseEntity.badRequest().build();
    }
}
