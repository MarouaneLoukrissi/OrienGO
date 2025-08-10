package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.Question.*;
import com.example.oriengo.mapper.QuestionMapper;
import com.example.oriengo.model.dto.QuestionDTO;
import com.example.oriengo.model.entity.Question;
import com.example.oriengo.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    //    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final MessageSource messageSource; // Injected

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public List<Question> getQuestions(boolean deleted) {
        try {
            log.info("Fetching questions with isDeleted = {}", deleted);
            List<Question> questions = questionRepository.findBySoftDeleted(deleted);
            log.info("Found {} questions", questions.size());
            return questions;
        } catch (Exception e) {
            log.error("Failed to fetch questions with isDeleted = {}: {}", deleted, e.getMessage(), e);
            throw new QuestionGetException(HttpStatus.NOT_FOUND, getMessage("question.not.found"));
        }
    }

    public Question getQuestionById(Long id, boolean deleted) {
        if (id == null) {
            log.warn("Attempted to fetch question with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("question.id.empty"));
        }

        log.info("Fetching question with ID: {} and isDeleted = {}", id, deleted);

        return questionRepository.findByIdAndSoftDeleted(id, deleted)
                .orElseThrow(() -> {
                    log.error("Question not found with ID: {} and isDeleted = {}", id, deleted);
                    return new QuestionGetException(HttpStatus.NOT_FOUND, getMessage("question.not.found"));
                });
    }

    public Question getQuestionById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch question with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("question.id.empty"));
        }

        log.info("Fetching question with ID: {}", id);

        return questionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Question not found with ID: {}", id);
                    return new QuestionGetException(HttpStatus.NOT_FOUND, getMessage("question.not.found"));
                });
    }

    @Transactional
    public void hardDeleteQuestion(Long id) {
        if (id == null) {
            log.warn("Attempted hard delete with null question ID");
            throw new QuestionDeleteException(HttpStatus.BAD_REQUEST, getMessage("question.id.empty"));
        }

        try {
            log.info("Attempting hard delete for question with ID: {}", id);

            Question question = getQuestionById(id);
            questionRepository.deleteById(question.getId());

            log.info("Successfully hard deleted question with ID: {}", question.getId());
        } catch (Exception e) {
            log.error("Error during hard delete of question with ID {}: {}", id, e.getMessage(), e);
            throw new QuestionDeleteException(HttpStatus.CONFLICT, getMessage("question.hard.delete.failed"));
        }
    }

    @Transactional
    public void softDeleteQuestion(Long id) {
        if (id == null) {
            log.warn("Attempted soft delete with null question ID");
            throw new QuestionDeleteException(HttpStatus.BAD_REQUEST, getMessage("question.id.empty"));
        }

        try {
            log.info("Attempting soft delete for question with ID: {}", id);

            Question question = getQuestionById(id, false);

            question.setSoftDeleted(true);
            questionRepository.save(question);

            log.info("Successfully soft deleted question with ID: {}", question.getId());
        } catch (Exception e) {
            log.error("Error during soft delete of question with ID {}: {}", id, e.getMessage(), e);
            throw new QuestionDeleteException(HttpStatus.CONFLICT, getMessage("question.soft.delete.failed"));
        }
    }

    @Transactional
    public Question restoreQuestion(Long id) {
        if (id == null) {
            log.warn("Attempted restore with null question ID");
            throw new QuestionRestoreException(HttpStatus.BAD_REQUEST, getMessage("question.id.empty"));
        }

        try {
            log.info("Attempting restore for question with ID: {}", id);

            Question question = getQuestionById(id, true);

            question.setSoftDeleted(false);

            questionRepository.save(question);

            log.info("Successfully restore question with ID: {}", question.getId());
            return question;
        } catch (Exception e) {
            log.error("Error during restore of question with ID {}: {}", id, e.getMessage(), e);
            throw new QuestionRestoreException(HttpStatus.BAD_REQUEST, getMessage("question.restore.failed"));
        }
    }

    @Transactional
    public Question createQuestion(QuestionDTO dto) {
        if (dto == null) {
            log.warn("Question DTO cannot be null");
            throw new QuestionCreationException(HttpStatus.BAD_REQUEST, getMessage("question.dto.empty"));
        }
        try {
            log.info("Starting creation of new question with text: {}", dto.getText());

            // Optional: Check if a question with the same text already exists (if desired)
            boolean exists = questionRepository.existsByTextAndSoftDeletedFalse(dto.getText());
            if (exists) {
                log.warn("Question already exists with text: {}", dto.getText());
                throw new QuestionCreationException(HttpStatus.CONFLICT, getMessage("question.text.already.exists", dto.getText()));
            }

            // Map DTO to entity
            Question question = questionMapper.toEntity(dto);


            Question savedQuestion = questionRepository.save(question);
            log.info("Question created successfully with ID: {}", savedQuestion.getId());

            return savedQuestion;

        } catch (QuestionCreationException e) {
            // Already logged, rethrow
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during question creation for text '{}': {}", dto.getText(), e.getMessage(), e);
            throw new QuestionCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("question.create.failed"));
        }
    }


    @Transactional
    public Question updateQuestion(Long id, QuestionDTO dto) {
        if (id == null) {
            log.warn("Attempted to update question with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("question.id.empty"));
        }
        if (dto == null) {
            log.warn("Question update request cannot be null");
            throw new QuestionUpdateException(HttpStatus.BAD_REQUEST, getMessage("question.dto.empty"));
        }

        try {
            log.info("Updating question with ID: {}", id);

            Question existingQuestion = getQuestionById(id);
            if (existingQuestion == null) {
                log.warn("Question not found with ID: {}", id);
                throw new QuestionUpdateException(HttpStatus.NOT_FOUND, getMessage("question.not.found", id));
            }

            questionMapper.updateQuestionFromDto(dto, existingQuestion);

            Question savedQuestion = questionRepository.save(existingQuestion);
            log.info("Question with ID {} successfully updated", savedQuestion.getId());
            return savedQuestion;

        } catch (Exception e) {
            log.error("Error updating question with ID {}: {}", id, e.getMessage(), e);
            throw new QuestionUpdateException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("question.update.failed"));
        }
    }
}
