package com.tiktok.project.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@Getter
public class EditProfileRequestDTO {
    @Min(value = 1, message = "userId must be greater than 0")
    private int id;
    private String avatarURL;
    private String username;
    private String fullName;
    private String bio;
}
