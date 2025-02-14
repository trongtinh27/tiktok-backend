package com.tiktok.project.service;

import com.tiktok.project.dto.request.EditProfileRequestDTO;
import com.tiktok.project.dto.response.FriendResponse;
import com.tiktok.project.dto.response.SearchResponse;
import com.tiktok.project.dto.response.UserDTO;
import com.tiktok.project.dto.response.UsernameValidationDTO;
import com.tiktok.project.entity.Role;
import com.tiktok.project.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserService {

    UserDTO loadUser(Authentication authentication);
    UserDTO getUserDTO(String username);
    UsernameValidationDTO checkUsername(String username);
    Object editProfile(EditProfileRequestDTO editProfileRequestDTO);
    List<FriendResponse> getListFriend(int id);
    List<SearchResponse.UserSearchInfo> search(String q);
    void saveUser(User user);
    void editUser(User user);
    void addToUser(User user, String roleName);
    User findUserById(int id);
    User findUserByEmail(String email);
    User findUserByUsername(String username);
    User findUserByPhoneNumber(String phone);
    UserDetails loadUserByUsername(String username);
    UserDetails loadUserByEmail(String email);
    UserDetails loadUserByPhoneNumber(String username);
    List<User> findUserByDisplayNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(String displayName, String username);


}
