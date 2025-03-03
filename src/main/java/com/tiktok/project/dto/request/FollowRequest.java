package com.tiktok.project.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FollowRequest {
    private int followerId;
    private int followedId;
    private String usernameFollowed;
}
