package com.tiktok.project.service.ImpService;

import com.tiktok.project.entity.Video;
import com.tiktok.project.repository.VideoRepository;
import com.tiktok.project.service.VideoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VideoImpService implements VideoService {

    @Autowired
    private VideoRepository videoRepository;
    @Override
    public List<Video> getAllVideo() {
        return videoRepository.findAll();
    }
}
