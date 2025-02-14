package com.tiktok.project.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FollowResponseDTO {
    private boolean isFollowing;  // Trạng thái follow
    private boolean isMutualFollowing; // Trạng thái follow lẫn nhau
    private int followingId; // Người được follow
    private int followerId;  // Người follow
}
