package com.tiktok.project.controller;

import com.tiktok.project.dto.response.*;
import com.tiktok.project.dto.request.CollectRequest;
import com.tiktok.project.dto.request.LikeRequest;
import com.tiktok.project.dto.request.UploadVideoRequest;
import com.tiktok.project.entity.Comment;
import com.tiktok.project.entity.User;
import com.tiktok.project.entity.Video;
import com.tiktok.project.service.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/video")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VideoController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FollowService followService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private UserService userService;

//    @GetMapping("/all/{userId}")
//    public ResponseSuccess getAllVideo(@PathVariable int userId ) {
//        List<Video> videoList = videoService.getAllVideo();
//
//        boolean followStatus;
//        boolean likeStatus;
//        boolean collectStatus;
//
//
//        List<VideoDTO> videoDTOS = new ArrayList<>();
//        for (Video video: videoList) {
//
//            if(userId == 0) {
//                followStatus = false;
//                likeStatus = false;
//                collectStatus = false;
//            } else {
//                followStatus = followService.checkFollowerWithFollowed(userId,video.getUser().getId());
//                likeStatus = likeService.isVideoLiked(userService.findUserById(userId), video.getId());
//                collectStatus = collectService.isVideoCollected(userService.findUserById(userId), video.getId());
//            }
//
//            videoDTOS.add(new VideoDTO(video.getId(),
//                    video.getUser().getId(),
//                    video.getUser().getUsername(),
//                    video.getUser().getProfilePictureUrl(),
//                    video.getThumbnailUrl(),
//                    video.getVideoUrl(),
//                    video.getDescription(),
//                    video.getShape(),
//                    video.getComments().size(),
//                    video.getLikesCount(),
//                    video.getCollectCount(),
//                    followStatus,
//                    likeStatus,
//                    collectStatus
//                    ));
//        }
//
//        return new ResponseSuccess(HttpStatus.OK, "Get all video", videoDTOS);
//    }

    @GetMapping("/getByUser")
    public ResponseData<?> getVideoByUser(@RequestParam String username) {
        log.info("Require get list video of user: {}", username);
        List<VideoDTO> dtoList = videoService.getVideoByUser(username);
        log.info("Get list video of user: {} successfully", username);
        return new ResponseData<>(HttpStatus.OK, "Get videos by " + username + " successfully", dtoList);
    }

    @GetMapping("/getByUsername&VideoId/{userId}")
    public ResponseData<?> getByUsernameAndVideoID(@PathVariable int userId,
                                                     @RequestParam @NotBlank String username,
                                                     @RequestParam @Min(value = 1, message = "VideoId have must be greater 0")  int videoId) {
        log.info("Require get video by Username&VideoID of user: {}", username);
        DetailVideoDTO detailVideoDTO = videoService.getVideoByUsernameAndVideoId(userId, username, videoId);
        log.info("Get list video of user: {} successfully", username);
        return new ResponseData<>(HttpStatus.OK, "Get videos by " + username + " successfully", detailVideoDTO);
    }

    @GetMapping("/feed")
    public ResponseData<?> getVideoFeed(Authentication authentication,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size) {
        log.info("Require get video feed");
        try {
            List<VideoDTO> dtoList = videoService.getVideoFeed(authentication, page, size);
            log.info("Get video feed successfully");
            return new ResponseData<>(HttpStatus.OK, "Get video feed successfully", dtoList);
        } catch (Exception e) {
            log.error("Get video feed failed: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(@RequestBody UploadVideoRequest videoRequest) {
        log.info("Require upload video");
        if(videoRequest == null) {
            log.error("Require upload video failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        videoService.addVideo(videoRequest);
        log.info("Upload Video successfully");
        return ResponseEntity.ok("Upload Video successfully");
    }

    @PostMapping("/like")
    public ResponseData<?> toggleLikeVideo(@RequestBody LikeRequest likeRequest) {
        log.info("Require like video {}", likeRequest.getVideoId());
        try {
            LikeResponse likeResponse = videoService.toggleLikeVideo(likeRequest);
            log.info("Like video {} successfully", likeRequest.getVideoId());
            return new ResponseData<>(HttpStatus.OK, "Like successfully", likeResponse);
        } catch (Exception e) {
            log.error("Like video {} failed", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST, "Like video " + likeRequest.getVideoId() + " failed");
        }
    }

    @PostMapping("/collect")
    public ResponseData<?> toggleCollectVideo(@RequestBody CollectRequest collectRequest) {
        log.info("Require collect video {}", collectRequest.getVideoId());
        try {
            CollectResponse collectResponse = videoService.toggleCollectVideo(collectRequest);
            log.info("Collect video {} successfully", collectRequest.getVideoId());
            return new ResponseData<>(HttpStatus.OK, "Collect successfully", collectResponse);
        } catch (Exception e) {
            log.error("Collect video {} failed", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST, "Collect video " + collectRequest.getVideoId() + " failed");
        }
    }
}
