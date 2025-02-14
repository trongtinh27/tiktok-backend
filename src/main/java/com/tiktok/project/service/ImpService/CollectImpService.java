package com.tiktok.project.service.ImpService;

import com.tiktok.project.entity.Collect;
import com.tiktok.project.entity.User;
import com.tiktok.project.entity.Video;
import com.tiktok.project.repository.CollectRepository;
import com.tiktok.project.repository.VideoRepository;
import com.tiktok.project.service.CollectService;
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
public class CollectImpService implements CollectService {

    private final CollectRepository collectRepository;
    private final VideoRepository videoRepository;

    @Override
    public boolean collectVideo(User user, int videoId) {
        Optional<Collect> existingCollect = collectRepository.findByUserAndVideoId(user, videoId);
        Video video = videoRepository.findById(videoId).orElse(null);

        if(video != null) {
            if(existingCollect.isPresent()) {
                videoRepository.save(video);
                collectRepository.delete(existingCollect.get());
                return false;
            }
            Collect collect = new Collect();
            collect.setVideo(video);
            collect.setUser(user);
            collect.setCreatedAt(new Date());
            videoRepository.save(video);
            collectRepository.save(collect);
            return true;
        }

        return false;
    }

    @Override
    public int getCountCollectVideo(int videoId) {
        return collectRepository.countByVideoId(videoId);
    }

    @Override
    public boolean isVideoCollected(User user, int videoId) {
        return collectRepository.findByUserAndVideoId(user,videoId).isPresent();
    }
}
