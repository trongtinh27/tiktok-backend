package com.tiktok.project.repository;

import com.tiktok.project.entity.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Integer> {
    List<Video> getVideosByUser_Username(String username);
    Optional<Video> getVideoByUser_UsernameAndId(String username, int videoId);

    @Query("SELECT v FROM Video v WHERE v.id NOT IN " +
            "(SELECT vv.video.id FROM View vv WHERE vv.user.id = :userId) ")
    List<Video> findUnseenVideos(int userId, Pageable pageable);
    @Query("SELECT v FROM Video v ORDER BY FUNCTION('MOD', CAST(v.id AS integer) * :seed, 50)")
    List<Video> findRandomVideosWithSeed(int seed, Pageable pageable);


}
