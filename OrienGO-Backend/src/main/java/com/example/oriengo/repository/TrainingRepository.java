package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    boolean existsByNameAndSoftDeletedFalse(String name);
}
