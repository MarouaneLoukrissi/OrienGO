package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByUserId(Long userId);
}
