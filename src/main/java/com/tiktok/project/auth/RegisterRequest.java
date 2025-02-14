package com.tiktok.project.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiktok.project.util.ValidAccount;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ValidAccount
public class RegisterRequest {
    @JsonProperty("isEmail")
    private boolean isEmail;

    @NotNull(message = "Birthday must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date birthday;

    @NotNull(message = "Account must be not null")
    private String account;

    @NotNull(message = "Password must not be null")
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;
}
