package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Job;
import com.example.oriengo.model.enumeration.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    List<Job> findByCategory(JobCategory category);
    
    List<Job> findByTitleContainingIgnoreCase(String title);
    
    List<Job> findBySoftDeletedFalse();
} 