package com.tiktok.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
public class RefreshTokenRequest {
    private String token;
}
