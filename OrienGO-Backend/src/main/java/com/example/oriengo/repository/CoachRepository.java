package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    Optional<Coach> findByIdAndIsDeleted(Long id, boolean deleted);
    Optional<Coach> findByEmailAndIsDeleted(String email, boolean deleted);
    List<Coach> findByIsDeleted(boolean deleted);
}
