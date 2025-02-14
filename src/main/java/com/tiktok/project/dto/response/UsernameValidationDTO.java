package com.tiktok.project.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UsernameValidationDTO {
    private boolean exists;
    private String message;
}
