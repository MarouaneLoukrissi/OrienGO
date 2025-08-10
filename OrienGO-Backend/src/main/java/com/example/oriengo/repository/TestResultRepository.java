package com.example.oriengo.repository;

import com.example.oriengo.model.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    
    // Récupérer tous les résultats non supprimés
    List<TestResult> findBySoftDeletedFalse();
    
    // Récupérer un résultat par ID de test
    @Query("SELECT tr FROM TestResult tr WHERE tr.test.id = :testId AND tr.softDeleted = false")
    Optional<TestResult> findByTestIdAndSoftDeletedFalse(@Param("testId") Long testId);
    
    // Récupérer tous les résultats d'un étudiant
    @Query("SELECT tr FROM TestResult tr JOIN tr.test t WHERE t.student.id = :studentId AND tr.softDeleted = false")
    List<TestResult> findByStudentIdAndSoftDeletedFalse(@Param("studentId") Long studentId);
}
