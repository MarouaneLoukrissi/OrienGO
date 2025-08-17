package com.example.oriengo.repository;

import com.example.oriengo.model.entity.PersonalizedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalizedJobRepository extends JpaRepository<PersonalizedJob, Long> {
    
    Optional<List<PersonalizedJob>> findByJobRecommendationId(Long jobRecommendationId);
    
    List<PersonalizedJob> findByHighlightedTrue();
}