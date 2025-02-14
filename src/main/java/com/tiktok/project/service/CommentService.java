package com.tiktok.project.service;

import com.tiktok.project.dto.request.CommentRequest;
import com.tiktok.project.dto.response.CommentDTO;
import com.tiktok.project.entity.Comment;
import com.tiktok.project.entity.Video;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentService {
    List<Comment> getCommentsByVideo(Video video);
    List<CommentDTO> getComments(int videoId);
    CommentDTO postComment(CommentRequest commentRequest);
}
