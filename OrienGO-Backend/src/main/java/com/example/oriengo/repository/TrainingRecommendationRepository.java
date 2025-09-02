package com.example.oriengo.repository;

import com.example.oriengo.model.entity.TestResult;
import com.example.oriengo.model.entity.TrainingRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRecommendationRepository extends JpaRepository<TrainingRecommendation, Long> {
    List<TrainingRecommendation> findByTestResult(TestResult testResult);
}
