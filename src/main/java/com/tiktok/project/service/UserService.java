package com.tiktok.project.service;

import com.tiktok.project.entity.Role;
import com.tiktok.project.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService {
    void saveUser(User user);
    Role saveRole(Role role);

    void editUser(User user);

    void addToUser(User user, String roleName);

    User findUserById(int id);
    User findUserByEmail(String email);
    User findUserByUsername(String username);
    User findUserByPhoneNumber(String phone);
    UserDetails loadUserByUsername(String username);
    UserDetails loadUserByEmail(String email);
    UserDetails loadUserByPhoneNumber(String username);



}
