package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Job;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.model.entity.StudentJobLink;
import com.example.oriengo.model.enumeration.LinkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentJobLinkRepository extends JpaRepository<StudentJobLink, Long> {
    
    Optional<List<StudentJobLink>> findByStudentId(Long studentId);
    
    Optional<List<StudentJobLink>> findByJobId(Long jobId);
    
    Optional<List<StudentJobLink>> findByType(String type);

    // Find a link by student, job, and type
    Optional<StudentJobLink> findByStudentAndJobAndType(Student student, Job job, LinkType type);

    // Optional: if you want to find *any* link regardless of type
    Optional<StudentJobLink> findByStudentAndJob(Student student, Job job);

    // Optional: if you want to check existence
    boolean existsByStudentAndJobAndType(Student student, Job job, LinkType type);

    // Optional: if you want to delete directly
    void deleteByStudentAndJobAndType(Student student, Job job, LinkType type);

    List<StudentJobLink> findAllByStudent_IdAndJob_Id(Long studentId, Long jobId);

    List<StudentJobLink> findAllByType(LinkType type);
}