package com.tiktok.project.controller;


import com.tiktok.project.dto.FollowResponseDTO;
import com.tiktok.project.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FollowController {
    @Autowired
    private FollowRepository followRepository;

    @GetMapping("/checkFollowing")
    public ResponseEntity<?> checkFollowing(@RequestParam int followingId,
                                                            @RequestParam int followerId) {
        // Kiểm tra followerId có follow followingId
        boolean isFollowing = followRepository.existsByFollowerIdAndFollowedId(followerId, followingId);

        // Kiểm tra ngược lại, followingId có follow followerId
        boolean isReverseFollowing = followRepository.existsByFollowerIdAndFollowedId(followingId, followerId);

        // Tạo response DTO
        FollowResponseDTO response = new FollowResponseDTO();
        response.setFollowing(isFollowing);  // Trạng thái followerId có follow followingId
        response.setMutualFollowing(isFollowing && isReverseFollowing);  // Cả 2 đều follow nhau
        response.setFollowingId(followingId);
        response.setFollowerId(followerId);

        // Trả về kết quả
        return ResponseEntity.ok(response);
    }

}
