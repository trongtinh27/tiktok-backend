package com.tiktok.project.repository;

import com.tiktok.project.entity.Collect;
import com.tiktok.project.entity.Like;
import com.tiktok.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectRepository extends JpaRepository<Collect, Integer> {
    Optional<Collect> findByUserAndVideoId(User user, int videoId);
    int countByVideoId(int videoId);
}
