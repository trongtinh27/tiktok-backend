package com.tiktok.project.dto.response;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FollowInfoResponseDTO {
    private String avatarURL;
    private String tiktokID;
    private String fullName;
    private boolean isVerify;
}
