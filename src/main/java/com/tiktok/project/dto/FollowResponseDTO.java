package com.tiktok.project.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FollowResponseDTO {
    private boolean isFollowing;  // Trạng thái follow
    private boolean isMutualFollowing; // Trạng thái follow lẫn nhau
    private int followingId; // Người được follow
    private int followerId;  // Người follow
}
