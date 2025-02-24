package com.tiktok.project.controller;


import com.tiktok.project.dto.request.FollowRequest;
import com.tiktok.project.dto.response.*;
import com.tiktok.project.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FollowController {

    private final FollowService followService;
    @GetMapping("/checkFollowing")
    public ResponseData<?> checkFollowing(@RequestParam int followingId,
                                          @RequestParam int followerId) {
        return new ResponseData<>(HttpStatus.OK, "Check follow successfully", followService.checkFollowing(followingId, followerId));
    }
    @GetMapping("/get-list-following")
    public ResponseData<?> getListFollowing(
            @RequestParam int id,
            @RequestParam int offset,
            @RequestParam int limit) {
        List<FollowInfoResponseDTO> followInfoResponseDTOList = followService.getListFollowingByUserId(id, offset, limit);
        if(followInfoResponseDTOList != null) {
            return new ResponseData<>(HttpStatus.OK, "Get List following by user " + id+ " successfully", followInfoResponseDTOList);
        }
        return new ResponseError(HttpStatus.BAD_REQUEST, "Get List following by user " + id+ " failed");
    }

    @GetMapping("/get-list-follower")
    public ResponseData<?> getListFollower(
            @RequestParam int id,
            @RequestParam int offset,
            @RequestParam int limit) {
        List<FollowInfoResponseDTO> followInfoResponseDTOList = followService.getListFollowerByUserId(id, offset, limit);
        if(followInfoResponseDTOList != null) {
            return new ResponseData<>(HttpStatus.OK, "Get List follower by user " + id+ " successfully", followInfoResponseDTOList);
        }
        return new ResponseError(HttpStatus.BAD_REQUEST, "Get List follower by user " + id+ " failed");
    }

    @PostMapping("/toggleFollow")
    public ResponseSuccess toggleFollow(@RequestBody FollowRequest followRequest) {
        return new ResponseSuccess(HttpStatus.OK, "Toggle Follow successfully", followService.toggleFollow(followRequest));
    }

}
