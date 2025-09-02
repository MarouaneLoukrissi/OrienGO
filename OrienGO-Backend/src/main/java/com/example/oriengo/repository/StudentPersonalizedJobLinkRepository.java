package com.example.oriengo.repository;

import com.example.oriengo.model.entity.StudentPersonalizedJobLink;
import com.example.oriengo.model.enumeration.LinkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentPersonalizedJobLinkRepository extends JpaRepository<StudentPersonalizedJobLink, Long> {
    // Find all links by student ID and personalized job ID
    List<StudentPersonalizedJobLink> findByStudent_IdAndPersonalizedJob_Id(Long studentId, Long personalizedJobId);

    // Optional: directly get only the types
    default List<LinkType> findLinkTypesByStudentIdAndPersonalizedJobId(Long studentId, Long personalizedJobId) {
        return findByStudent_IdAndPersonalizedJob_Id(studentId, personalizedJobId)
                .stream()
                .map(StudentPersonalizedJobLink::getType)
                .toList();
    }
}

