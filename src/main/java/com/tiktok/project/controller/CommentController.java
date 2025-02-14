package com.tiktok.project.controller;


import com.tiktok.project.dto.request.CommentRequest;
import com.tiktok.project.dto.response.CommentDTO;
import com.tiktok.project.dto.response.ResponseData;
import com.tiktok.project.dto.response.ResponseError;
import com.tiktok.project.exception.ResourceNotFoundException;
import com.tiktok.project.service.CommentService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/comments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/get/{videoId}")
    public ResponseData<?> getComments(@PathVariable @Min(value=1, message = "Video id have must be greater than 0") int videoId) {
        log.info("Request get comment with videoId {}", videoId);
        return new ResponseData<>(HttpStatus.OK, "Get list comment videoId: " + videoId + " successfully", commentService.getComments(videoId));
    }

    @PostMapping("/post")
    public ResponseData<?> postComment(@RequestBody CommentRequest commentRequest) {
        log.info("Request post comment with videoId {}", commentRequest.getVideoId());
        try {
            CommentDTO res = commentService.postComment(commentRequest);
            log.info("Post comment with videoId {} successfully", commentRequest.getVideoId());
            return new ResponseData<>(HttpStatus.OK, "Post comment videoId: " + commentRequest.getVideoId() + " successfully", res);
        } catch (ResourceNotFoundException e) {
            log.error("Failed to load data: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
