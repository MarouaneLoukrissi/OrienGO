package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByIsDeleted(boolean deleted);
    Optional<Student> findByIdAndIsDeleted(Long id, boolean deleted);
    Optional<Student> findByEmailAndIsDeleted(String email, boolean deleted);

    boolean existsByEmailAndIsDeletedFalse(String originalEmail);

    Optional<Student> findByIdAndIsDeletedFalse(Long studentId);
}
