package com.tiktok.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
public class UploadVideoRequest {
    @NotNull(message = "userId must be not null")
    private int userId;
    @NotNull(message = "videoUrl must be not null")
    private String videoUrl;
    @NotNull(message = "thumbnailUrl must be not null")
    private String thumbnailUrl;
    @NotNull(message = "description must be not null")
    private String description;
    @NotNull(message = "shape must be not null")
    private String shape;

}
