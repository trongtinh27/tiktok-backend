package com.tiktok.project.controller;

import com.tiktok.project.auth.AuthenticationErrorResponse;
import com.tiktok.project.auth.AuthenticationRequest;
import com.tiktok.project.auth.AuthenticationResponse;
import com.tiktok.project.auth.RegisterRequest;
import com.tiktok.project.dto.UserDTO;
import com.tiktok.project.dto.request.RefreshTokenRequest;
import com.tiktok.project.entity.RefreshToken;
import com.tiktok.project.entity.User;
import com.tiktok.project.service.JwtService;
import com.tiktok.project.service.RefreshTokenService;
import com.tiktok.project.service.UserService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getAccount(), authRequest.getPassword())
            );
            if(authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                User principal = (User) authentication.getPrincipal();
                String username = principal.getUsername();

                RefreshToken exitsingToken = refreshTokenService.findByUserId(principal.getId()).orElse(null);
                Instant now = Instant.now();

                if(exitsingToken != null ) {
                    if(exitsingToken.getExpiryDate().isAfter(now)) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                                new AuthenticationErrorResponse(
                                        "Your account is logged in from another location. Do you want to log in here and log out from the other location?",
                                        "Conflict"
                                )
                        );
                    } else {
                    refreshTokenService.delete(exitsingToken);

                    }

                }
                String token = jwtService.generateToken(username);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(username);
                ResponseCookie jwtRefreshToken = jwtService.generateRefreshJwtCookie(refreshToken.getToken());
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, jwtRefreshToken.toString())
                        .body(
                                new AuthenticationResponse(
                                        token,
                                        "Login successfully",
                                        JwtService.EXPIRATIONTIME_TOKEN));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
            }
        }
        catch (BadCredentialsException e) {
            AuthenticationErrorResponse errorResponse = new AuthenticationErrorResponse(
                    "Invalid credential",
                    "Unauthorized"
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
        catch (UsernameNotFoundException e) {
            AuthenticationErrorResponse errorResponse = new AuthenticationErrorResponse(
                    "User not found",
                    "Unauthorized"
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping(value = "/confirmLogin")
    public ResponseEntity<?> confirmLogin(@RequestBody AuthenticationRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getAccount(), authRequest.getPassword())
            );
            if(authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                User principal = (User) authentication.getPrincipal();
                String username = principal.getUsername();

                RefreshToken exitsingToken = refreshTokenService.findByUserId(principal.getId()).orElse(null);

                if(exitsingToken != null) {
                    refreshTokenService.delete(exitsingToken);
                }
                String token = jwtService.generateToken(username);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(username);
                ResponseCookie jwtRefreshToken = jwtService.generateRefreshJwtCookie(refreshToken.getToken());
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, jwtRefreshToken.toString())
                        .body(
                                new AuthenticationResponse(
                                        token,
                                        "Login successfully",
                                        JwtService.EXPIRATIONTIME_TOKEN));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
            }
        }
        catch (BadCredentialsException e) {
            AuthenticationErrorResponse errorResponse = new AuthenticationErrorResponse(
                    "Invalid credential",
                    "Unauthorized"
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
        catch (UsernameNotFoundException e) {
            AuthenticationErrorResponse errorResponse = new AuthenticationErrorResponse(
                    "User not found",
                    "Unauthorized"
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {

        String refreshToken = jwtService.getRefreshFromCookies(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token is missing");
        }

        // Xử lý refreshToken
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user.getUsername());
                    return ResponseEntity.ok()
                            .body( new AuthenticationResponse(
                                    accessToken,
                                    "Refresh token successfully",
                                    JwtService.EXPIRATIONTIME_TOKEN
                            ));
                }).orElse(ResponseEntity.badRequest().build()); // Refresh token is not in database
    }



    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user;
        if(request.isEmail()) {
            user = userService.findUserByEmail(request.getAccount());
        } else {
            user = userService.findUserByPhoneNumber(request.getAccount());
        }

        if(user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }

        user = new User();
        user.setEmail(request.isEmail() ? request.getAccount() : "");
        user.setPhoneNumber(request.isEmail() ? "" : request.getAccount());
        user.setUsername(request.isEmail() ? request.getAccount().substring(0, request.getAccount().indexOf("@")) : request.getAccount());
        user.setDisplayName(request.isEmail() ? request.getAccount().substring(0, request.getAccount().indexOf("@")) : request.getAccount());
        user.setPassword(request.getPassword());
        user.setBirthDay(request.getBirthday());
        user.setVerify(false);
        user.setRoleList(new ArrayList<>());

        userService.addToUser(user, "ROLE_USER");


        userService.saveUser(user);

        return login(new AuthenticationRequest(user.getUsername(), user.getPassword()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!principle.toString().equals("anonymousUser")) {
            int userId = ((User) principle).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtRefreshCookie = jwtService.getCleanJwtRefreshCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body("You've been signed out!");
    }



}
