package com.tiktok.project.dto.response;

import com.tiktok.project.entity.Comment;
import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailVideoDTO {
    private int id;
    private int userId;
    private String username;
    private String displayName;
    private String userAvatar;
    private String thumbnailUrl;
    private String videoUrl;
    private String description;
    private String shape;
    private int commentCount;
    private int likeCount;
    private int collectCount;
    private boolean followStatus;
    private boolean likeStatus;
    private boolean collectStatus;
    private List<CommentDTO> commentList;
}
