package com.tiktok.project.controller;

import com.tiktok.project.auth.AuthenticationRequest;
import com.tiktok.project.auth.RegisterRequest;
import com.tiktok.project.dto.response.ResponseData;
import com.tiktok.project.dto.response.ResponseError;
import com.tiktok.project.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/login")
    public ResponseData<?> login(@RequestBody AuthenticationRequest authRequest) {
        log.info("Login request received for username: {}", authRequest.getAccount());
        ResponseData<?> response = new ResponseData<>(HttpStatus.OK, "Login Successfully", authenticationService.authenticate(authRequest));
        log.info("Account: {} login Successfully", authRequest.getAccount());
        return response;
    }


    @PostMapping(value = "/confirmLogin")
    public ResponseData<?> confirmLogin(@RequestBody AuthenticationRequest authRequest) {
        log.info("Confirm login request for username: {}", authRequest.getAccount());
        ResponseData<?> response = new ResponseData<>(HttpStatus.OK, "Confirm Login successfully", authenticationService.confirmLogin(authRequest));
        log.info("Account: {} login Successfully", authRequest.getAccount());
        return response;
    }

    @PostMapping("/refreshToken")
    public ResponseData<?> refreshToken(HttpServletRequest request) {
        log.info("Refresh token request received.");
        try {
            log.info("Refresh token Successfully");
            return new ResponseData<>(HttpStatus.OK, "Refresh token Successfully",authenticationService.refreshToken(request));
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST, "Refresh token failed");
        }
    }

    @PostMapping("/register")
    public ResponseData<?> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request received: {}", request.getAccount());
        ResponseData<?> response = new ResponseData<>(HttpStatus.OK, "Register successfully", authenticationService.register(request));
        log.info("Register response: {}", response);
        return response;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam @Min(value = 1 ) int id) {
        log.info("Logout request received.");
        ResponseEntity<?> response = authenticationService.logout(id);
        log.info("User {} logout successfully", response.getBody());
        return response;
    }



}
