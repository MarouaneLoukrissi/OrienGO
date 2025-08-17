package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM questions WHERE soft_deleted = false ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestions(@Param("limit") int limit);
    
    @Query(value = "SELECT * FROM questions WHERE category = :category AND soft_deleted = false ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestionsByCategory(@Param("category") String category, @Param("limit") int limit);

    List<Question> findBySoftDeleted(boolean deleted);

    Optional<Question> findByIdAndSoftDeleted(Long id, boolean deleted);

    boolean existsByTextAndSoftDeletedFalse(String text);
}