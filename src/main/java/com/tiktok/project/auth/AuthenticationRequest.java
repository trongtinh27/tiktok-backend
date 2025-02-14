package com.tiktok.project.auth;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotNull(message = "Account must be not null")
    private String account;
    @Min(value = 8, message = "Password must have at least 8 characters")
    private String password;
}
