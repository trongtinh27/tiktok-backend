package com.tiktok.project.service.ImpService;

import com.tiktok.project.dto.request.CommentRequest;
import com.tiktok.project.dto.response.CommentDTO;
import com.tiktok.project.entity.Comment;
import com.tiktok.project.entity.User;
import com.tiktok.project.entity.Video;
import com.tiktok.project.exception.ResourceNotFoundException;
import com.tiktok.project.repository.CommentRepository;
import com.tiktok.project.repository.UserRepository;
import com.tiktok.project.repository.VideoRepository;
import com.tiktok.project.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentImpService implements CommentService {

    private final CommentRepository commentRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @Override
    public List<Comment> getCommentsByVideo(Video video) {
        return commentRepository.getCommentsByVideo(video).orElse(null);
    }

    @Override
    public List<CommentDTO> getComments(int videoId) {
        List<Comment> commentList = commentRepository.findByVideoIdOrderByCreatedAtDesc(videoId);

        List<CommentDTO> commentDTOS = new ArrayList<>();

        for (Comment comment : commentList) {
            commentDTOS.add(
                    CommentDTO.builder()
                            .id(comment.getId())
                            .displayName(comment.getUser().getDisplayName())
                            .username(comment.getUser().getUsername())
                            .avatarUser(comment.getUser().getProfilePictureUrl())
                            .content(comment.getContent())
                            .create_at(comment.getCreatedAt())
                            .build()
            );
        }
        return commentDTOS;
    }

    @Override
    public CommentDTO postComment(CommentRequest commentRequest) {
        Comment savedComment = new Comment();
        User user = userRepository.findUserById(commentRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Video video = videoRepository.findById(commentRequest.getVideoId()).orElseThrow(() -> new ResourceNotFoundException("Video not found"));

        savedComment.setUser(user);
        savedComment.setVideo(video);
        savedComment.setContent(commentRequest.getContent());
        savedComment.setLikesCount(0);

        commentRepository.save(savedComment);

        CommentDTO res = CommentDTO.builder()
                .id(savedComment.getId())
                .displayName(savedComment.getUser().getDisplayName())
                .username(savedComment.getUser().getUsername())
                .avatarUser(savedComment.getUser().getProfilePictureUrl())
                .content(savedComment.getContent())
                .create_at(savedComment.getCreatedAt())
                .build();

        messagingTemplate.convertAndSend("/topic/comments/" + commentRequest.getVideoId(), res);

        return res;
    }
}
