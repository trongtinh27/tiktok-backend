package com.tiktok.project.service.ImpService;

import com.tiktok.project.dto.response.FollowInfoResponseDTO;
import com.tiktok.project.dto.request.FollowRequest;
import com.tiktok.project.dto.response.FollowResponseDTO;
import com.tiktok.project.dto.response.ToggleFollowResponse;
import com.tiktok.project.entity.Follower;
import com.tiktok.project.entity.User;
import com.tiktok.project.exception.ResourceNotFoundException;
import com.tiktok.project.repository.FollowRepository;
import com.tiktok.project.repository.UserRepository;
import com.tiktok.project.service.FollowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FollowImpService implements FollowService {
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public FollowResponseDTO checkFollowing(int followingId, int followerId) {
        log.info("---------- CheckFollowing ----------");
        // Kiểm tra followerId có follow followingId
        boolean isFollowing = followRepository.existsByFollowerIdAndFollowedId(followerId, followingId);
        log.info("Checking if user {} follows user {}", followerId, followingId);

        // Kiểm tra ngược lại, followingId có follow followerId
        boolean isReverseFollowing = followRepository.existsByFollowerIdAndFollowedId(followingId, followerId);
        log.info("Checking reverse: user {} follows user {}", followingId, followerId);

        return FollowResponseDTO.builder()
                .isFollowing(isFollowing)
                .isMutualFollowing(isFollowing && isReverseFollowing)
                .followingId(followingId)
                .followerId(followerId)
                .build();
    }

    @Override
    public boolean checkFollowerWithFollowed(int followerId, int followedId) {
        return followRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Override
    public List<User> getMutualFollowings(User user) {
        // Lấy danh sách người dùng mà user theo dõi
        Set<User> followingUsers = user.getFollowingRelations().stream()
                .map(Follower::getFollowed)
                .collect(Collectors.toSet());

        // Lấy danh sách follower của user
        Set<User> followerUsers = user.getFollowerRelations().stream()
                .map(Follower::getFollower)
                .collect(Collectors.toSet());

        // Tìm những người mà cả follower và following đều theo dõi nhau
        followingUsers.retainAll(followerUsers); // Giữ lại chỉ những người mà cả hai danh sách đều có

        return new ArrayList<>(followingUsers);
    }

    @Override
    public List<FollowInfoResponseDTO> getListFollowingByUserId(int id, int offset, int limit) {
        User user = userRepository.findUserById(id).orElse(null);
        if (user != null) {
            log.info("Get List following by user {}", id);

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
            return listResult;
        }
        log.info("Get List following error not found User {}", id);
        return null;
    }

    @Override
    public List<FollowInfoResponseDTO> getListFollowerByUserId(int id, int offset, int limit) {
        User user = userRepository.findUserById(id).orElse(null);
        if (user != null) {
            log.info("Get List follower by user {}", id);

            List<Follower> listFollower = user.getFollowerRelations()
                    .stream()
                    .skip(offset)
                    .limit(limit)
                    .toList();

            List<FollowInfoResponseDTO> listResult = new ArrayList<>();
            for (Follower follower : listFollower) {
                User userFollower = follower.getFollower();
                listResult.add(new FollowInfoResponseDTO(
                        userFollower.getProfilePictureUrl(),
                        userFollower.getUsername(),
                        userFollower.getDisplayName(),
                        userFollower.isVerify()
                ));
            }
            return listResult;
        }
        log.info("Get List follower error not found User {}", id);
        return null;
    }
    @Override
    public ToggleFollowResponse toggleFollow(FollowRequest followRequest) {
        log.info("---------- Toggle Following ----------");
        User followedUser = userRepository.findUserById(followRequest.getFollowerId()).orElse(null);
        User followingUser;
        if(followRequest.getUsernameFollowed() == null || followRequest.getUsernameFollowed().isEmpty()) {
            followingUser = userRepository.findUserById(followRequest.getFollowedId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {
            followingUser = userRepository.findUserById(followRequest.getFollowerId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }

        if (followedUser == null || followingUser == null) {
            log.warn("Not found user {} and user {}", followRequest.getFollowerId(), followRequest.getFollowedId());
            return ToggleFollowResponse.builder()
                    .followerId(0)
                    .followedId(0)
                    .isFollowing(false)
                    .totalFollowers(0)
                    .message("User not found")
                    .build();
        }

        Optional<Follower> existingFollowRelation = followRepository.findByFollowedAndFollower(followedUser, followingUser);

        if (existingFollowRelation.isPresent()) {
            Follower followRelation = existingFollowRelation.get();

            // Xóa khỏi danh sách quan hệ trước khi xóa khỏi database
            followingUser.getFollowingRelations().remove(followRelation);
            followedUser.getFollowerRelations().remove(followRelation);
//            userRepository.save(f)
            followRepository.delete(followRelation);
            int totalFollowers = followRepository.countByFollowed(followedUser);
            log.info("User {} unfollowed User {}", followedUser.getId(), followingUser.getId());

            return ToggleFollowResponse.builder()
                    .followerId(followingUser.getId())
                    .followedId(followedUser.getId())
                    .isFollowing(false)
                    .totalFollowers(totalFollowers)
                    .message("Unfollowed")
                    .build();
        }
        else {
            Follower newFollowRelation = new Follower();
            newFollowRelation.setFollower(followingUser);
            newFollowRelation.setFollowed(followedUser);
            newFollowRelation.setCreatedAt(new Date());

            followRepository.saveAndFlush(newFollowRelation);

            int totalFollowers = followRepository.countByFollowed(followedUser);
            log.info("User {} followed User {}", followedUser.getId(), followingUser.getId());
            return ToggleFollowResponse.builder()
                    .followerId(followingUser.getId())
                    .followedId(followedUser.getId())
                    .isFollowing(true)
                    .totalFollowers(totalFollowers)
                    .message("Followed")
                    .build();
        }
    }
}
