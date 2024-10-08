package com.tiktok.project.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationErrorResponse {
    private String message;
    private String error;
}
