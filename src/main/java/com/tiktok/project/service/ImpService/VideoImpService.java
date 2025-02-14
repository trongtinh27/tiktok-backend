package com.tiktok.project.service.ImpService;

import com.tiktok.project.dto.request.CollectRequest;
import com.tiktok.project.dto.request.LikeRequest;
import com.tiktok.project.dto.request.UploadVideoRequest;
import com.tiktok.project.dto.response.*;
import com.tiktok.project.entity.Comment;
import com.tiktok.project.entity.User;
import com.tiktok.project.entity.Video;
import com.tiktok.project.exception.ResourceNotFoundException;
import com.tiktok.project.repository.VideoRepository;
import com.tiktok.project.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VideoImpService implements VideoService {

    private final VideoRepository videoRepository;
    private final FollowService followService;
    private final LikeService likeService;
    private final CollectService collectService;
    private final UserService userService;
    private final CommentService commentService;
    private final JwtService jwtService;
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

    @Override
    public List<VideoDTO> getVideoByUser(String username) {
        List<Video> videoList = videoRepository.getVideosByUser_Username(username);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (Video video: videoList) {
            videoDTOS.add(convertVideoToDto(video,
                    followService.checkFollowerWithFollowed(video.getUser().getId(),video.getUser().getId()),
                    likeService.isVideoLiked(userService.findUserById(video.getUser().getId()), video.getId()),
                    collectService.isVideoCollected(userService.findUserById(video.getUser().getId()), video.getId())));
        }
        return videoDTOS;
    }

    @Override
    public DetailVideoDTO getVideoByUsernameAndVideoId(int userId, String username, int videoId) {
        Video video = videoRepository.getVideoByUser_UsernameAndId(username, videoId).orElseThrow(() -> new ResourceNotFoundException("Video not found"));
        List<CommentDTO> listCommentDTO = new ArrayList<>();
        List<Comment> listComment = commentService.getCommentsByVideo(video);
        if(listComment != null) {
            for (Comment comment: listComment) {
                listCommentDTO.add(new CommentDTO(comment.getId(),
                        comment.getUser().getDisplayName(),
                        comment.getUser().getUsername(),
                        comment.getUser().getProfilePictureUrl(),
                        comment.getContent(),
                        comment.getCreatedAt()));
            }
        }
        boolean  followStatus = false;
        boolean likeStatus = false;
        boolean collectStatus = false;

        if(userId != 0) {
            followStatus = followService.checkFollowerWithFollowed(userId,video.getUser().getId());
            likeStatus = likeService.isVideoLiked(userService.findUserById(userId), video.getId());
            collectStatus = collectService.isVideoCollected(userService.findUserById(userId), video.getId());
        }

        return  new DetailVideoDTO(video.getId(),
                video.getUser().getId(),
                video.getUser().getUsername(),
                video.getUser().getDisplayName(),
                video.getUser().getProfilePictureUrl(),
                video.getThumbnailUrl(),
                video.getVideoUrl(),
                video.getDescription(),
                video.getShape(),
                video.getCommentCount(),
                video.getLikeCount(),
                video.getCollectCount(),
                followStatus,
                likeStatus,
                collectStatus,
                listCommentDTO
        );
    }

    @Override
    public List<VideoDTO> getVideoFeed(Authentication authentication, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Video> videos;
        List<VideoDTO> dtoList = new ArrayList<>();
        int seed = 2772;
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            videos = videoRepository.findUnseenVideos(user.getId(), pageable);

            if(videos.isEmpty()) {
                seed = user.hashCode();
                videos = videoRepository.findRandomVideosWithSeed(seed, pageable);
            }
            for (Video video: videos) {
                dtoList.add(convertVideoToDto(video,
                        followService.checkFollowerWithFollowed(user.getId(),video.getUser().getId()),
                        likeService.isVideoLiked(userService.findUserById(user.getId()), video.getId()),
                        collectService.isVideoCollected(userService.findUserById(user.getId()), video.getId())
                        ));
            }
        } else
            {
                videos = videoRepository.findRandomVideosWithSeed(seed, pageable);
                for (Video video: videos) {
                    dtoList.add(convertVideoToDto(video, false, false, false));

                }
            }
            return dtoList;
    }

    @Override
    public LikeResponse toggleLikeVideo(LikeRequest likeRequest){
        User user = userService.findUserById(likeRequest.getUserId());
            LikeResponse likeResponse = new LikeResponse();
            likeResponse.setLikeStatus(likeService.likeVideo(user, likeRequest.getVideoId()));
            likeResponse.setCountLike(likeService.getCountLikeVideo(likeRequest.getVideoId()));
            return likeResponse;
    }

    @Override
    public CollectResponse toggleCollectVideo(CollectRequest collectRequest) {
        User user = userService.findUserById(collectRequest.getUserId());
        CollectResponse collectResponse = new CollectResponse();
        collectResponse.setCollectStatus(collectService.collectVideo(user, collectRequest.getVideoId()));
        collectResponse.setCountCollect(collectService.getCountCollectVideo(collectRequest.getVideoId()));
        return collectResponse;
    }

    private VideoDTO convertVideoToDto(Video video, boolean followStatus, boolean likeStatus, boolean collectStatus) {
        return new VideoDTO(video.getId(),
                video.getUser().getId(),
                video.getUser().getUsername(),
                video.getUser().getProfilePictureUrl(),
                video.getThumbnailUrl(),
                video.getVideoUrl(),
                video.getDescription(),
                video.getShape(),
                video.getViewsCount(),
                video.getCommentCount(),
                video.getLikeCount(),
                video.getCollectCount(),
                followStatus,
                likeStatus,
                collectStatus);
    }

}
