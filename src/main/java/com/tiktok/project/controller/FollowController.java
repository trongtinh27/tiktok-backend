package com.tiktok.project.controller;


import com.tiktok.project.dto.FollowInfoResponseDTO;
import com.tiktok.project.dto.FollowResponseDTO;
import com.tiktok.project.entity.Follower;
import com.tiktok.project.entity.User;
import com.tiktok.project.repository.FollowRepository;
import com.tiktok.project.service.FollowService;
import com.tiktok.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/follow")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @GetMapping("/checkFollowing")
    public ResponseEntity<?> checkFollowing(@RequestParam int followingId,
                                                            @RequestParam int followerId) {
        // Kiểm tra followerId có follow followingId
        boolean isFollowing = followService.checkFollowerWithFollowed(followerId, followingId);

        // Kiểm tra ngược lại, followingId có follow followerId
        boolean isReverseFollowing = followService.checkFollowerWithFollowed(followingId, followerId);

        // Tạo response DTO
        FollowResponseDTO response = new FollowResponseDTO();
        response.setFollowing(isFollowing);  // Trạng thái followerId có follow followingId
        response.setMutualFollowing(isFollowing && isReverseFollowing);  // Cả 2 đều follow nhau
        response.setFollowingId(followingId);
        response.setFollowerId(followerId);

        // Trả về kết quả
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-list-following")
    public ResponseEntity<?> getListFollowing(
            @RequestParam int id,
            @RequestParam int offset,
            @RequestParam int limit) {

        User user = userService.findUserById(id);
        if (user != null) {
            List<Follower> listFollowing = user.getFollowingRelations()
                    .stream()
                    .skip(offset)
                    .limit(limit)
                    .toList();

            List<FollowInfoResponseDTO> listResult = new ArrayList<>();
            for (Follower follower : listFollowing) {
                User userFollowing = follower.getFollowed();
                listResult.add(new FollowInfoResponseDTO(
                        userFollowing.getProfilePictureUrl(),
                        userFollowing.getUsername(),
                        userFollowing.getDisplayName(),
                        userFollowing.isVerify()
                ));
            }
            return ResponseEntity.ok(listResult);
        }



        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/get-list-follower")
    public ResponseEntity<?> getListFollower(
            @RequestParam int id,
            @RequestParam int offset,
            @RequestParam int limit) {

        User user = userService.findUserById(id);
        if (user != null) {
            List<Follower> listFollower = user.getFollowerRelations()
                    .stream()
                    .skip(offset)
                    .limit(limit)
                    .toList();

            List<FollowInfoResponseDTO> listResult = new ArrayList<>();
            for (Follower follower : listFollower) {
                User userFollower = follower.getFollowed();
                listResult.add(new FollowInfoResponseDTO(
                        userFollower.getProfilePictureUrl(),
                        userFollower.getUsername(),
                        userFollower.getDisplayName(),
                        userFollower.isVerify()
                ));
            }
            return ResponseEntity.ok(listResult);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
