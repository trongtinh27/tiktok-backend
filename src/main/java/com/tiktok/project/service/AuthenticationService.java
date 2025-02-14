package com.tiktok.project.service;

import com.tiktok.project.auth.AuthenticationErrorResponse;
import com.tiktok.project.auth.AuthenticationRequest;
import com.tiktok.project.auth.AuthenticationResponse;
import com.tiktok.project.auth.RegisterRequest;
import com.tiktok.project.dto.response.ResponseSuccess;
import com.tiktok.project.entity.RedisToken;
import com.tiktok.project.entity.RefreshToken;
import com.tiktok.project.entity.User;
import com.tiktok.project.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;


    /**
     * Xử lý xác thực người dùng, tạo token
     */
    public Object authenticate(AuthenticationRequest authRequest) {
        log.info("---------- authenticate ----------");

        User user = validateAndAuthenticateUser(authRequest);
        RefreshToken existingToken = refreshTokenService.findByUserId(user.getId()).orElse(null);


        if ((existingToken != null && existingToken.getExpiryDate().isAfter(Instant.now())) || redisTokenService.isExits(user.getUsername())) {
            log.warn("User {} is already logged in from another location.", user.getUsername());
            return buildConflictResponse();
        }

        if (existingToken != null) {
//            refreshTokenService.delete(existingToken);
            redisTokenService.remove(user.getUsername());
        }

        return buildAuthenticationResponse(user);
    }

    /**
     * Xác thực lại đăng nhập, tạo token
     */
    public Object confirmLogin(AuthenticationRequest authRequest) {
        User user = validateAndAuthenticateUser(authRequest);
        refreshTokenService.findByUserId(user.getId())
                .ifPresent(refreshTokenService::delete);
        if(redisTokenService.isExits(user.getUsername())) {
            redisTokenService.remove(user.getUsername());
        }

        return buildAuthenticationResponse(user);
    }

    /**
     *
     */
    public Object refreshToken(HttpServletRequest request) {
        log.info("---------- refreshToken ----------");
        String refreshToken = request.getHeader("Refresh-token");
        final String username = jwtService.extractUsername(refreshToken);
        var user = userService.findUserByUsername(username);
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new ResourceNotFoundException("Token must be not blank");
        }

        if(!jwtService.validateToken(refreshToken, user)) {
            throw new ResourceNotFoundException("Not allow access with this token");
        }
        String accessToken = jwtService.generateToken(user.getUsername());
        return AuthenticationResponse.builder()
                .token(accessToken)
                .tokenExpiration(JwtService.EXPIRATIONTIME_TOKEN)
                .build();
    }

    public Object register(RegisterRequest request) {
        User user;
        if(request.isEmail()) {
            user = userService.findUserByEmail(request.getAccount());
        } else {
            user = userService.findUserByPhoneNumber(request.getAccount());
        }

        if(user != null) {
            return AuthenticationErrorResponse.builder()
                    .status(400)
                    .error("Exists")
                    .message("User already exists")
                    .build();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        String randomSuffix = generateRandomNumber();
        user = new User();
        user.setEmail(request.isEmail() ? request.getAccount() : "");
        user.setPhoneNumber(request.isEmail() ? "" : request.getAccount());
        user.setUsername(request.isEmail() ? request.getAccount().substring(0, request.getAccount().indexOf("@")) + randomSuffix : request.getAccount() + randomSuffix);
        user.setDisplayName(request.isEmail() ? request.getAccount().substring(0, request.getAccount().indexOf("@")) + randomSuffix : request.getAccount() + randomSuffix);
        user.setPassword(request.getPassword());
        user.setBirthDay(request.getBirthday());
        user.setVerify(false);
        user.setRoleList(new ArrayList<>());

        userService.addToUser(user, "ROLE_USER");
        userService.saveUser(user);

        return buildAuthenticationResponse(user);
    }

    /**
     * Đăng xuất
     */
    public ResponseEntity<?> logout(int id) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!principle.toString().equals("anonymousUser")) {
            var user = ((User) principle);
            refreshTokenService.deleteByUserId(id);
            redisTokenService.remove(user.getUsername());
        }
//        redisTokenService.remove(user.getUsername());
        refreshTokenService.deleteByUserId(id);

        ResponseCookie jwtRefreshCookie = jwtService.getCleanJwtRefreshCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(id);
    }

    /**
     * Xác thực thông tin đăng nhập của người dùng
     */
    private User validateAndAuthenticateUser(AuthenticationRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getAccount(), authRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User principal = (User) authentication.getPrincipal();
        return userService.findUserByUsername(principal.getUsername());
    }

    /**
     * Builds the authentication response with tokens.
     */
    private AuthenticationResponse buildAuthenticationResponse(User user) {
        String token = jwtService.generateToken(user.getUsername());
//        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername()).getToken();
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        redisTokenService.save(RedisToken.builder().id(user.getUsername()).refreshToken(refreshToken).expiration((long) JwtService.EXPIRATIONTIME_REFRESHTOKEN).build());
        log.info("User {} logged in successfully.", user.getUsername());
        return AuthenticationResponse.builder()
                .userId(user.getId())
                .token(token)
                .refreshToken(refreshToken)
                .tokenExpiration(JwtService.EXPIRATIONTIME_TOKEN)
                .refreshTokenExpiration(JwtService.EXPIRATIONTIME_REFRESHTOKEN)
                .build();
    }

    /**
     * Builds a conflict response when user is logged in from another location.
     */
    private AuthenticationErrorResponse buildConflictResponse() {
        return AuthenticationErrorResponse.builder()
                .status(409)
                .message("Your account is logged in from another location. Do you want to log in here and log out from the other location?")
                .error("Conflict")
                .build();
    }
    /**
     * Random number
     */
    private String generateRandomNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int digit = random.nextInt(10); // Random từ 0-9
            sb.append(digit);
        }

        return sb.toString();
    }
}
