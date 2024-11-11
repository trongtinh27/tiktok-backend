package com.tiktok.project.service.ImpService;

import com.tiktok.project.entity.Follower;
import com.tiktok.project.entity.User;
import com.tiktok.project.repository.FollowRepository;
import com.tiktok.project.service.FollowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FollowImpService implements FollowService {
    @Autowired
    private FollowRepository followRepository;

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
}
