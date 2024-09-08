package com.tiktok.project.service.ImpService;

import com.tiktok.project.entity.Role;
import com.tiktok.project.entity.User;
import com.tiktok.project.repository.RoleRepository;
import com.tiktok.project.repository.UserRepository;
import com.tiktok.project.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserImpService implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addToUser(User user, String roleName) {
        Role role = roleRepository.findRoleByName(roleName);
        user.getRoleList().add(role);
    }

    @Override
    public User findUserById(int id) {
        Optional<User> user = userRepository.findUserById(id);
        return user.orElse(null);
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

//    @Override
//    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
//        return null; // Không sử dụng phương thức này
//    }
}
