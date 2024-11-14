package com.tiktok.project.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FollowRequest {
    private int followerId;
    private int followedId;
}
