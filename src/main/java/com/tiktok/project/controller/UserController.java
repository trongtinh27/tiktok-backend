package com.tiktok.project.controller;

import com.tiktok.project.dto.EditProfileRequestDTO;
import com.tiktok.project.dto.UserDTO;
import com.tiktok.project.dto.UsernameValidationDTO;
import com.tiktok.project.entity.User;
import com.tiktok.project.service.UserService;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> loadUser(Authentication authentication) {
        try {
            if(authentication== null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            Object principal = authentication.getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = principal.toString();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected principal type");
            }

            return getResponseEntity(username);
        } catch (SignatureException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");

        }
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<?> getProfileByUsername(@PathVariable String username) {
        try {
            if(username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found user");
            }

            return getResponseEntity(username);
        } catch (SignatureException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");

        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        User user = userService.findUserByUsername(username);
        if(user == null) {
            return ResponseEntity.ok(new UsernameValidationDTO(
                    false,
                    "Username is available"
            ));
        }
        return ResponseEntity.ok(
                new UsernameValidationDTO(
                        true,
                        "User already exists"
                )
        );
    }

    @PostMapping("/edit-profile")
    public ResponseEntity<?> editProfile(@RequestBody EditProfileRequestDTO editProfileRequest) {
        System.out.println(editProfileRequest);
        User user = userService.findUserById(editProfileRequest.getId());
        if(user != null) {
            user.setProfilePictureUrl(editProfileRequest.getAvatarURL());
            user.setUsername(editProfileRequest.getUsername());
            user.setDisplayName(editProfileRequest.getFullName());
            user.setBio(editProfileRequest.getBio());
            userService.editUser(user);

            return getResponseEntity(user.getUsername());
        }
        return ResponseEntity.badRequest().body("Edit profile fail");
    }

    private ResponseEntity<?> getResponseEntity(String username) {
        User user = userService.findUserByUsername(username);
        if(user != null) {
            return ResponseEntity.ok(
                    new UserDTO(
                            user.getId(),
                            user.getUsername(),
                            user.getDisplayName(),
                            user.getProfilePictureUrl(),
                            user.getBio(),
                            user.getFollowingCount(),
                            user.getFollowingCount(),
                            user.isVerify(),
                            user.getRoleList()
                    )
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found User");
    }
}
