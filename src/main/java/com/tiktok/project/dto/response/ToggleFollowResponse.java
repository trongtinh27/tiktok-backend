package com.tiktok.project.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToggleFollowResponse {
    private int followerId;  // ID của người thực hiện hành động
    private int followedId;  // ID của người được theo dõi
    private boolean isFollowing;  // true nếu đã follow, false nếu đã unfollow
    private int totalFollowers;  // Tổng số người theo dõi của `followedUser`
    private String message;  // Thông báo trạng thái

}
