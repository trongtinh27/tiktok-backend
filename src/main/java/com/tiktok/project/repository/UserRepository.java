package com.tiktok.project.repository;

import com.tiktok.project.entity.Role;
import com.tiktok.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);
    User findUserByUsername(String username);
//    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(int id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
    /*
    SELECT DISTINCT u.*
    FROM users u
    WHERE LOWER(u.display_name) LIKE '%query%'
       OR LOWER(u.username) LIKE '%query%';
     */
    Optional<List<User>> findDistinctByDisplayNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(String displayName, String username);
    @Query(value = "select r.name from User u join u.roleList r where u.id = :userId")
    List<String> findAllRolesByUserId(@Param("userId") int userId);


}
