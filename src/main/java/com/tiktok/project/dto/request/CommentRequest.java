package com.tiktok.project.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentRequest {
    private int userId;
    private int videoId;
    private String content;
}
