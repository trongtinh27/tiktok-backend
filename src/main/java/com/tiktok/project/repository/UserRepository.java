package com.tiktok.project.repository;

import com.tiktok.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);
    User findUserByUsername(String username);
    Optional<User> findUserById(int id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);



}
