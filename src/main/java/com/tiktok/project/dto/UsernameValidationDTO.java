package com.tiktok.project.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UsernameValidationDTO {
    private boolean exists;
    private String message;
}
