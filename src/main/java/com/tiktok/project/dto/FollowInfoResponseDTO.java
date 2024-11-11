package com.tiktok.project.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FollowInfoResponseDTO {
    private String avatarURL;
    private String tiktokID;
    private String fullName;
    private boolean isVerify;
}
