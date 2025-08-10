package com.example.oriengo.repository;

import com.example.oriengo.model.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {

    boolean existsByQuestionIdAndText(Long questionId, String text);
}
