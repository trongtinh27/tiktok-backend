package com.tiktok.project.service.ImpService;

import com.tiktok.project.entity.Like;
import com.tiktok.project.entity.User;
import com.tiktok.project.entity.Video;
import com.tiktok.project.repository.LikeRepository;
import com.tiktok.project.repository.VideoRepository;
import com.tiktok.project.service.LikeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikeImpService implements LikeService {
    private final LikeRepository likeRepository;
    private final VideoRepository videoRepository;

    @Override
    public boolean likeVideo(User user, int videoId) {
        Optional<Like> existingLike = likeRepository.findByUserAndVideoId(user, videoId);
        Video video = videoRepository.findById(videoId).orElse(null);

        if(video != null) {
            if(existingLike.isPresent()) {
                videoRepository.save(video);
                likeRepository.delete(existingLike.get());
                return false;
            }
            Like like = new Like();
            like.setUser(user);
            like.setVideo(video);
            like.setCreatedAt(new Date());
            videoRepository.save(video);
            likeRepository.save(like);
            return true;
        }

        return false;
    }

    @Override
    public int getCountLikeVideo(int videoId) {
        return likeRepository.countByVideoId(videoId);
    }

    @Override
    public boolean isVideoLiked(User user, int videoId) {
        return likeRepository.findByUserAndVideoId(user, videoId).isPresent();
    }
}
