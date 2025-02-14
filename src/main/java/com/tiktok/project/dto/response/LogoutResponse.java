package com.tiktok.project.dto.response;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@RequiredArgsConstructor
public class LogoutResponse {
    private int userId;
    private String message;
}
