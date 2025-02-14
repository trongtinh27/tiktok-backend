package com.tiktok.project.service.ImpService;

import com.sun.java.accessibility.util.Translator;
import com.tiktok.project.dto.request.EditProfileRequestDTO;
import com.tiktok.project.dto.response.FriendResponse;
import com.tiktok.project.dto.response.SearchResponse;
import com.tiktok.project.dto.response.UserDTO;
import com.tiktok.project.dto.response.UsernameValidationDTO;
import com.tiktok.project.entity.Role;
import com.tiktok.project.entity.User;
import com.tiktok.project.exception.ResourceNotFoundException;
import com.tiktok.project.repository.FollowRepository;
import com.tiktok.project.repository.RoleRepository;
import com.tiktok.project.repository.UserRepository;
import com.tiktok.project.service.FollowService;
import com.tiktok.project.service.UserService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserImpService implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FollowService followService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO loadUser(Authentication authentication) {
        log.info("------------Load User------------");
        try {
            if(authentication== null) {
                throw new IllegalArgumentException("Authentication cannot be null");
            }
            Object principal = authentication.getPrincipal();
            String username = null;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = principal.toString();
            }
            return getUserDTO(username);

        } catch (SignatureException e) {
            throw new SignatureException("Invalid JWT token");
        }
    }
    @Override
    public UserDTO getUserDTO(String username) {
        User user = userRepository.findUserByUsername(username);
        if(user == null) throw new ResourceNotFoundException("User not found")   ;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getDisplayName())
                .avatarURL(user.getProfilePictureUrl())
                .bio(user.getBio())
                .followingCount(user.getFollowingCount())
                .followerCount(user.getFollowerCount())
                .verify(user.isVerify())
                .roles(user.getRoleList())
                .build();
    }

    @Override
    public UsernameValidationDTO checkUsername(String username) {
        User user = findUserByUsername(username);
        if(user == null) {
            return UsernameValidationDTO.builder()
                    .exists(false)
                    .message("Username is available")
                    .build();
        }
        return UsernameValidationDTO.builder()
                .exists(true)
                .message("User already exists")
                .build();
    }

    @Override
    public Object editProfile(EditProfileRequestDTO editProfileRequestDTO) {
        User user = userRepository.findUserById(editProfileRequestDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setProfilePictureUrl(editProfileRequestDTO.getAvatarURL());
        user.setDisplayName(editProfileRequestDTO.getFullName());
        user.setBio(editProfileRequestDTO.getBio());
        editUser(user);

        return getUserDTO(user.getUsername());
    }

    @Override
    public List<FriendResponse> getListFriend(int id) {
        User user = findUserById(id);
        List<User> userList = followService.getMutualFollowings(user);
        List<FriendResponse> responses = new ArrayList<>();

        for (User friend : userList) {
            FriendResponse friendResponse = new FriendResponse(
                    friend.getId(),
                    friend.getUsername(),
                    friend.getDisplayName(),
                    friend.getProfilePictureUrl()
            );
            responses.add(friendResponse);
        }
        return responses;
    }

    @Override
    public List<SearchResponse.UserSearchInfo> search(String q) {
        List<User> userList = findUserByDisplayNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(q,q);
        List<SearchResponse.UserSearchInfo> userSearchInfos = new ArrayList<>();
        for (User user : userList) {
            userSearchInfos.add(
                    SearchResponse.UserSearchInfo.builder()
                            .nickname(user.getUsername())
                            .fullName(user.getDisplayName())
                            .avatar(user.getProfilePictureUrl())
                            .tick(user.isVerify())
                            .build()
            );
        }
        return userSearchInfos;
    }


    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void editUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void addToUser(User user, String roleName) {
        Role role = roleRepository.findRoleByName(roleName);
        user.getRoleList().add(role);
    }

    @Override
    public User findUserById(int id) {
        return  userRepository.findUserById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByPhoneNumber(String phone) {
        Optional<User> user = userRepository.findByPhoneNumber(phone);
        return user.orElse(null);
    }

    //    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByUsername(username);
        return userDetail.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByEmail(email);
        return userDetail.orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    @Override
    public UserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByPhoneNumber(phoneNumber);
        return userDetail.orElseThrow(() -> new UsernameNotFoundException("User not found: " + phoneNumber));
    }

    @Override
    public List<User> findUserByDisplayNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(String displayName, String username) {
        return userRepository.findDistinctByDisplayNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(displayName, username).orElse(null);
    }

//    @Override
//    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
//        return null; // Không sử dụng phương thức này
//    }



}
