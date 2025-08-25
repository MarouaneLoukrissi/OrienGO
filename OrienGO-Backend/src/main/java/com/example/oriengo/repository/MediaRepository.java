package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Media;
import com.example.oriengo.model.enumeration.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByUserId(Long userId);

    @Query("SELECT m FROM Media m WHERE m.user.id = :userId AND m.type = 'COVER_PHOTO' ORDER BY m.createdAt DESC LIMIT 1")
    Optional<Media> findLatestCoverByUserId(@Param("userId") Long userId);

    @Query("SELECT m FROM Media m WHERE m.user.id = :userId AND m.type = 'PROFILE_PHOTO' ORDER BY m.createdAt DESC LIMIT 1")
    Optional<Media> findLatestProfileByUserId(@Param("userId") Long userId);

    Optional<Media> findFirstByUserIdAndTypeOrderByCreatedAtDesc(Long userId, MediaType type);
}
