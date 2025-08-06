package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {
    // Récupère uniquement les tests non soft-deleted
    List<Test> findBySoftDeletedFalse();
    
    // Récupère un test avec ses relations
    @Query("SELECT t FROM Test t LEFT JOIN FETCH t.questions LEFT JOIN FETCH t.student WHERE t.id = :id AND t.softDeleted = false")
    Optional<Test> findByIdWithRelations(@Param("id") Long id);
    
    // Récupère tous les tests avec leurs relations
    @Query("SELECT t FROM Test t LEFT JOIN FETCH t.questions LEFT JOIN FETCH t.student WHERE t.softDeleted = false")
    List<Test> findAllWithRelations();
}
