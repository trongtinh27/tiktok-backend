package com.tiktok.project.service.ImpService;

import com.tiktok.project.entity.Follower;
import com.tiktok.project.entity.User;
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
//
//    @Override
//    public boolean followUser(User user, User follower) {
//        List<Follower> listFollowing = follower.getFollowingRelations();
//        Optional<Follower> existingFollowing = followRepository.findByFollowedAndFollower(user,follower);
//        if(existingFollowing.isPresent()){
//            if(listFollowing.contains(existingFollowing.get())){
//                follower.getFollowingRelations().remove(existingFollowing.get());
//
//                userRepository.save(follower);
//                followRepository.delete(existingFollowing.get());
//                return true;
//            }
//        }
//
//        return false;
//    }

    @Override
    public boolean toggleFollow(User followedUser, User followingUser) {
        // Danh sách những người mà `followingUser` đang theo dõi
        List<Follower> followingRelations = followingUser.getFollowingRelations();

        // Kiểm tra xem `followingUser` đã theo dõi `followedUser` hay chưa
        Optional<Follower> existingFollowRelation = followRepository.findByFollowedAndFollower(followedUser, followingUser);

        // Nếu quan hệ follow đã tồn tại
        if (existingFollowRelation.isPresent()) {
            Follower followRelation = existingFollowRelation.get();

            // Kiểm tra xem quan hệ này có nằm trong danh sách `followingRelations` của `followingUser` không
            if (followingRelations.contains(followRelation)) {
                followingUser.getFollowingRelations().remove(existingFollowRelation.get());

                userRepository.save(followingUser);
                followRepository.delete(followRelation);
                return false;  // Đã xóa follow thành công
            }
        } else {
            // Nếu quan hệ chưa tồn tại, tạo quan hệ follow mới
            Follower newFollowRelation = new Follower();
            newFollowRelation.setFollower(followingUser);
            newFollowRelation.setFollowed(followedUser);
            newFollowRelation.setCreatedAt(new Date());

            followRepository.save(newFollowRelation);
            return true;  // Đã thêm follow thành công
        }

        return false;  // Không có thay đổi
    }
}
