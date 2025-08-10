package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Test;
import com.example.oriengo.model.enumeration.TestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    // Récupère uniquement les tests non soft-deleted
    List<Test> findBySoftDeletedFalse();

    // Récupère un test avec ses relations (testQuestions and their questions, and student)
    @Query("SELECT DISTINCT t FROM Test t " +
            "LEFT JOIN FETCH t.testQuestions tq " +
            "LEFT JOIN FETCH tq.question q " +
            "LEFT JOIN FETCH t.student " +
            "WHERE t.id = :id AND t.softDeleted = :deleted")
    Optional<Test> findByIdWithRelations(@Param("id") Long id, @Param("deleted") boolean deleted);

    @Query("SELECT DISTINCT t FROM Test t " +
            "LEFT JOIN FETCH t.testQuestions tq " +
            "LEFT JOIN FETCH tq.question q " +
            "LEFT JOIN FETCH tq.chosenAnswer ca " +
            "LEFT JOIN FETCH t.student " +
            "WHERE t.id = :id AND t.softDeleted = :deleted")
    Optional<Test> findByIdWithFullRelations(@Param("id") Long id, @Param("deleted") boolean deleted);

    @Query("SELECT DISTINCT t FROM Test t " +
            "LEFT JOIN FETCH t.testQuestions tq " +
            "LEFT JOIN FETCH tq.question q " +
            "LEFT JOIN FETCH t.student " +
            "WHERE t.id = :id")
    Optional<Test> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT DISTINCT t FROM Test t " +
            "LEFT JOIN FETCH t.testQuestions tq " +
            "LEFT JOIN FETCH tq.question q " +
            "LEFT JOIN FETCH t.student " +
            "WHERE t.student.id = :id AND t.softDeleted = :deleted")
    List<Test> findAllByStudentIdWithRelations(@Param("id") Long id, @Param("deleted") boolean deleted);

    @Query("SELECT DISTINCT t FROM Test t " +
            "LEFT JOIN FETCH t.testQuestions tq " +
            "LEFT JOIN FETCH tq.question q " +
            "LEFT JOIN FETCH t.student " +
            "WHERE t.softDeleted = :deleted")
    List<Test> findAllWithRelations(@Param("deleted") boolean deleted);

    @Query("SELECT DISTINCT t FROM Test t " +
            "LEFT JOIN FETCH t.testQuestions tq " +
            "LEFT JOIN FETCH tq.question q " +
            "LEFT JOIN FETCH t.student " +
            "WHERE t.student.id = :id AND t.status = :status AND t.softDeleted = :deleted")
    List<Test> findAllByStudentIdAndStatusWithRelations(@Param("id") Long id, @Param("status") TestStatus status, @Param("deleted") boolean deleted);
}
