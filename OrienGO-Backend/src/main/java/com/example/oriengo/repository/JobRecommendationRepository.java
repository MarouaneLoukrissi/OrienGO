package com.example.oriengo.repository;

import com.example.oriengo.model.entity.JobRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRecommendationRepository extends JpaRepository<JobRecommendation, Long> {
}