package com.tiktok.project.dto.response;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendResponse {
    private int id;
    private String username;
    private String fullname;
    private String avatar;

}
