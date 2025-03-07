package com.tiktok.project.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Collection;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractEntity<Integer> implements UserDetails  {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "display_name")
    private String displayName;

//    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;


    @Column(name = "password_hash", nullable = false)
    @JsonIgnore // 🔥 Không trả password ra JSON
    private String password;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(length = 500)
    private String bio;

    @Column(name = "birthday", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay;


    @Column(name = "updated_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)  // 🔥 Đổi EAGER → LAZY
    @JsonIgnore
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roleList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // 🔥 Tránh vòng lặp JSON
    private List<Video> videos;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-comments")
    private List<Comment> comments;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Follower> followingRelations;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Follower> followerRelations;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Message> sentMessages;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Message> receivedMessages;

    @Column(name = "is_verify", nullable = false)
    private boolean isVerify;


    // Getter cho follower count
    public int getFollowerCount() {
        return followerRelations != null ? followerRelations.size() : 0;
    }

    // Getter cho following count
    public int getFollowingCount() {
        return followingRelations != null ? followingRelations.size() : 0;
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList.stream()
                .map(role -> (GrantedAuthority) () -> role.getName())
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(String email, String password) {
        this.email = email;
        this.username = email;
        this.password = password;
    }
}
