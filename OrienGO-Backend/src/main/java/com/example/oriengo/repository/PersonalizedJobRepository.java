package com.example.oriengo.repository;

import com.example.oriengo.model.entity.PersonalizedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalizedJobRepository extends JpaRepository<PersonalizedJob, Long> {
    
    List<PersonalizedJob> findByJobRecommendationId(Long jobRecommendationId);
    
    List<PersonalizedJob> findByHighlightedTrue();
}