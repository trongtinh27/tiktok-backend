package com.tiktok.project.dto;

import com.tiktok.project.entity.Role;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private int id;
    private String username;
    private String fullName;
    private String avatarURL;
    private String bio;
    private int followingCount;
    private int followerCount;
    private boolean verify;
    private List<Role> roles;
}
