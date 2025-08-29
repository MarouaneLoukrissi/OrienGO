package com.example.oriengo.repository;

import com.example.oriengo.model.entity.StudentTrainingLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTrainingLinkRepository extends JpaRepository<StudentTrainingLink, Long> {
}
