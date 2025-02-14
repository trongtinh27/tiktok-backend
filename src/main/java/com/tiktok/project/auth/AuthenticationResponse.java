package com.tiktok.project.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private int userId;
    private String token;
    private String refreshToken;
    private int tokenExpiration;
    private int refreshTokenExpiration;

}
