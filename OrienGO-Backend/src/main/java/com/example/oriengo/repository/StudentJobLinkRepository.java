package com.example.oriengo.repository;

import com.example.oriengo.model.entity.StudentJobLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentJobLinkRepository extends JpaRepository<StudentJobLink, Long> {
    
    Optional<List<StudentJobLink>> findByStudentId(Long studentId);
    
    Optional<List<StudentJobLink>> findByJobId(Long jobId);
    
    Optional<List<StudentJobLink>> findByType(String type);
} 