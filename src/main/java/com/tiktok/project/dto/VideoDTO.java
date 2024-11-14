package com.tiktok.project.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VideoDTO {
    private int id;
    private int userId;
    private String username;
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


}
