package com.tiktok.project.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UploadVideoRequest {
    private int userId;
    private String videoUrl;
    private String thumbnailUrl;
    private String description;
    private String shape;

}
