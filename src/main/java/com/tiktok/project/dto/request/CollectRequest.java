package com.tiktok.project.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@Getter
public class CollectRequest {
    @NotNull(message = "userId must be not null")
    private int userId;
    @NotNull(message = "videoId must be not null")
    private int videoId;
}
