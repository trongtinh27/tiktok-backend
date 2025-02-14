package com.tiktok.project.service.ImpService;

import com.tiktok.project.entity.User;
import com.tiktok.project.entity.Video;
import com.tiktok.project.entity.View;
import com.tiktok.project.exception.ResourceNotFoundException;
import com.tiktok.project.repository.VideoRepository;
import com.tiktok.project.repository.ViewRepository;
import com.tiktok.project.service.VideoService;
import com.tiktok.project.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewImpService implements ViewService {
    private final ViewRepository viewRepository;
    private final VideoRepository videoRepository;

    @Override
    public void saveView(Authentication authentication, int videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new ResourceNotFoundException("Video not found"));
        if(authentication == null) throw new ResourceNotFoundException("User not found");
        User user = (User) authentication.getPrincipal();
        if(!viewRepository.existsByUserAndVideo(user, video)) {
            View view = new View();
            view.setUser(user);
            view.setVideo(video);
            viewRepository.save(view);
        }

    }
}
