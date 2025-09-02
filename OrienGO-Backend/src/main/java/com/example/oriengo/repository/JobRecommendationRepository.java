package com.example.oriengo.repository;

import com.example.oriengo.model.entity.JobRecommendation;
import com.example.oriengo.model.entity.PersonalizedJob;
import com.example.oriengo.model.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRecommendationRepository extends JpaRepository<JobRecommendation, Long> {
    List<JobRecommendation> findByTestResult(TestResult testResult);
}