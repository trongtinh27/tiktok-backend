package com.tiktok.project.repository;

import com.tiktok.project.entity.Comment;
import com.tiktok.project.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<List<Comment>> getCommentsByVideo(Video video);
    List<Comment> findByVideoIdOrderByCreatedAtDesc(int videoId);
}
