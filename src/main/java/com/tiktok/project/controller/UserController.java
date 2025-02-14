package com.tiktok.project.controller;

import com.tiktok.project.dto.request.EditProfileRequestDTO;
import com.tiktok.project.dto.response.*;
import com.tiktok.project.exception.ResourceNotFoundException;
import com.tiktok.project.service.UserService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@Validated
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseData<?> loadUser(Authentication authentication) {
        log.info("Request get user profile");
        try {
            UserDTO userDTO = userService.loadUser(authentication);
            log.info("Load user profile successfully, username={}", userDTO.getUsername());

            return new ResponseData<>(HttpStatus.OK, "Get Profile successfully", userDTO);
        } catch (SignatureException e) {
            log.error("JWT signature validation failed: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/get/{username}")
    public ResponseData<?> getProfileByUsername(@PathVariable
                                                      @Size(min = 3, max = 30, message = "Username must be from 3 to 30 characters") String username) {
        log.info("Request get user profile, username={}", username);
        try {
            return new ResponseData<>(HttpStatus.OK, "Get Profile successfully", userService.getUserDTO(username));
        } catch (ResourceNotFoundException e) {
            log.error("Failed to load user profile: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST, "Get Profile failed");
        }
    }

    @GetMapping("/check-username")
    public ResponseData<?> checkUsername(@RequestParam String username) {
        log.info("Request to check username: {}", username);
        var usernameValidationDTO = userService.checkUsername(username);
        log.info("Username '{}' is {} available", username, usernameValidationDTO.isExists() ? "" : "not ");
        return new ResponseData<>(HttpStatus.OK, "Check username successfully", usernameValidationDTO);
    }

    @PostMapping("/edit-profile")
    public ResponseData<?> editProfile(@Valid @RequestBody EditProfileRequestDTO editProfileRequest) {
        log.info("Request update userId={}", editProfileRequest.getId());
        try {
            return new ResponseData<>(HttpStatus.OK, "Edit profile successfully", userService.editProfile(editProfileRequest));
        } catch (ResourceNotFoundException e) {
            log.error("Failed to edit user profile: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/get-list-friend")
    public ResponseData<?> getListFriend(@RequestParam @Min(value = 1, message = "Id have must be greater than 0") int id) {
        log.info("Request to get list of friends for userId={}", id);
        try {
            var friends = userService.getListFriend(id);
            log.info("Successfully retrieved {} friends for userId={}", friends.size(), id);
            return new ResponseData<>(HttpStatus.OK, "Get List Friend successfully", friends);
        } catch (ResourceNotFoundException e) {
            log.error("Failed to get list of friends for userId={}, reason: {}", id, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseData<?> searchUser(@RequestParam @NotBlank String q) {
        log.info("Request to search users with keyword='{}'", q);
        var result = userService.search(q);
        log.info("Search completed for keyword='{}', found {} users", q, result.size());
        return new ResponseData<>(HttpStatus.OK, "Search user successfully", result);
    }
}
