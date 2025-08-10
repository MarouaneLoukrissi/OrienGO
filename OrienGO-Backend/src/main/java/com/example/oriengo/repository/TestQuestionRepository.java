package com.example.oriengo.repository;

import com.example.oriengo.model.entity.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

    @Query("""
        SELECT tq FROM TestQuestion tq
        JOIN FETCH tq.question q
        LEFT JOIN FETCH q.answerOptions ao
        LEFT JOIN FETCH tq.chosenAnswer ca
        WHERE tq.test.id = :testId
    """)
    Set<TestQuestion> findAllByTestIdWithQuestionAndChosenAnswer(@Param("testId") Long testId);}
