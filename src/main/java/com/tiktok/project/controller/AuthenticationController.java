package com.tiktok.project.controller;

import com.tiktok.project.auth.AuthenticationErrorResponse;
import com.tiktok.project.auth.AuthenticationRequest;
import com.tiktok.project.auth.AuthenticationResponse;
import com.tiktok.project.auth.RegisterRequest;
import com.tiktok.project.dto.UserDTO;
import com.tiktok.project.entity.User;
import com.tiktok.project.service.JwtService;
import com.tiktok.project.service.UserService;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/login")
    public ResponseEntity<?> loginWithEmail(@RequestBody AuthenticationRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getAccount(), authRequest.getPassword())
            );
            if(authentication.isAuthenticated()) {
                User principal = (User) authentication.getPrincipal();
                String username = principal.getUsername();
                String token = jwtService.generateToken(username);
                String refreshToken = jwtService.generateRefreshToken(username);
                return ResponseEntity.ok(new AuthenticationResponse(token, refreshToken, "Login successfully", JwtService.EXPIRATIONTIME_TOKEN));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
            }
        }
        catch (BadCredentialsException e) {
            AuthenticationErrorResponse errorResponse = new AuthenticationErrorResponse(
                    "Invalid credential",
                    "Unauthorized"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        catch (UsernameNotFoundException e) {
            AuthenticationErrorResponse errorResponse = new AuthenticationErrorResponse(
                    "User not found",
                    "Unauthorized"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
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
        String token = jwtService.generateToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(token, refreshToken,
                "Register successfully", JwtService.EXPIRATIONTIME_TOKEN));
    }


}
