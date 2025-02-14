package com.tiktok.project.dto.response;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponse {
    private boolean likeStatus;
    private int countLike;
}
