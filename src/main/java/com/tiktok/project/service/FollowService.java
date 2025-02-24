package com.tiktok.project.service;

import com.tiktok.project.dto.response.FollowInfoResponseDTO;
import com.tiktok.project.dto.request.FollowRequest;
import com.tiktok.project.dto.response.FollowResponseDTO;
import com.tiktok.project.dto.response.ToggleFollowResponse;
import com.tiktok.project.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FollowService {

    FollowResponseDTO checkFollowing(int followingId, int followerId);
    List<FollowInfoResponseDTO> getListFollowingByUserId(int id, int offset, int limit);
    List<FollowInfoResponseDTO> getListFollowerByUserId(int id, int offset, int limit);
    boolean checkFollowerWithFollowed(int followerId, int followedId);
    List<User> getMutualFollowings(User user);
    ToggleFollowResponse toggleFollow(FollowRequest followRequest);


}
