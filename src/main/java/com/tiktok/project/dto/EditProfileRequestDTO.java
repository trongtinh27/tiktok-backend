package com.tiktok.project.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EditProfileRequestDTO {
    private int id;
    private String avatarURL;
    private String username;
    private String fullName;
    private String bio;
}
