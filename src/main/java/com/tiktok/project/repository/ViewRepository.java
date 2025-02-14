package com.tiktok.project.repository;

import com.tiktok.project.entity.User;
import com.tiktok.project.entity.Video;
import com.tiktok.project.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepository extends JpaRepository<View, Integer> {
    boolean existsByUserAndVideo(User user, Video video);

}
