package com.tiktok.project.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationErrorResponse {
    private int status;
    private String message;
    private String error;
}
