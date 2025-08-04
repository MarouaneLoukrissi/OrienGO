package com.example.oriengo.repository;

import com.example.oriengo.model.entity.StudentJobLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentJobLinkRepository extends JpaRepository<StudentJobLink, Long> {
    
    List<StudentJobLink> findByStudentId(Long studentId);
    
    List<StudentJobLink> findByJobId(Long jobId);
    
    List<StudentJobLink> findByType(String type);
} 