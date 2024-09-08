package com.tiktok.project.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @JsonProperty("isEmail")
    private boolean isEmail;
    private Date birthday;
    private String account;
    private String password;
}
