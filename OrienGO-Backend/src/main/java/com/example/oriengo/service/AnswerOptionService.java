package com.example.oriengo.service;

import com.example.oriengo.exception.custom.AnswerOption.AnswerOptionCreationException;
import com.example.oriengo.exception.custom.AnswerOption.AnswerOptionDeleteException;
import com.example.oriengo.exception.custom.AnswerOption.AnswerOptionGetException;
import com.example.oriengo.exception.custom.AnswerOption.AnswerOptionUpdateException;
import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.mapper.AnswerOptionMapper;
import com.example.oriengo.model.dto.AnswerOptionDTO;
import com.example.oriengo.model.entity.*;
import com.example.oriengo.repository.AnswerOptionRepository;
import com.example.oriengo.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class AnswerOptionService {
    private final AnswerOptionRepository answerOptionRepository;
    private final AnswerOptionMapper answerOptionMapper;
    //    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final MessageSource messageSource; // Injected
    private final QuestionRepository questionRepository;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public Set<AnswerOption> getAnswerOptions() {
        try {
            log.info("Fetching answerOptions");
            Set<AnswerOption> answerOptions = (Set<AnswerOption>) answerOptionRepository.findAll();
            log.info("Found {} answerOptions", answerOptions.size());
            return answerOptions;
        } catch (Exception e) {
            log.error("Failed to fetch answerOptions}: {}", e.getMessage(), e);
            throw new AnswerOptionGetException(HttpStatus.NOT_FOUND, getMessage("answerOption.not.found"));
        }
    }

    public AnswerOption getAnswerOptionById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch answerOption with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("answerOption.id.empty"));
        }

        log.info("Fetching answerOption with ID: {}", id);

        return answerOptionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("AnswerOption not found with ID: {}", id);
                    return new AnswerOptionGetException(HttpStatus.NOT_FOUND, getMessage("answerOption.not.found"));
                });
    }

    @Transactional
    public void hardDeleteAnswerOption(Long id) {
        if (id == null) {
            log.warn("Attempted hard delete with null answerOption ID");
            throw new AnswerOptionDeleteException(HttpStatus.BAD_REQUEST, getMessage("answerOption.id.empty"));
        }

        try {
            log.info("Attempting hard delete for answerOption with ID: {}", id);

            AnswerOption answerOption = getAnswerOptionById(id);
            answerOptionRepository.deleteById(answerOption.getId());

            log.info("Successfully hard deleted answerOption with ID: {}", answerOption.getId());
        } catch (Exception e) {
            log.error("Error during hard delete of answerOption with ID {}: {}", id, e.getMessage(), e);
            throw new AnswerOptionDeleteException(HttpStatus.CONFLICT, getMessage("answerOption.hard.delete.failed"));
        }
    }

    @Transactional
    public AnswerOption createAnswerOption(AnswerOptionDTO dto) {
        if (dto == null) {
            log.warn("AnswerOption DTO cannot be null");
            throw new AnswerOptionCreationException(HttpStatus.BAD_REQUEST, getMessage("answerOption.dto.empty"));
        }
        try {
            log.info("Starting creation of new answerOption with text: {}", dto.getText());

            // Check if the Question exists
            Question question = questionRepository.findById(dto.getQuestionId())
                    .orElseThrow(() -> {
                        log.warn("Question not found with ID: {}", dto.getQuestionId());
                        return new AnswerOptionCreationException(HttpStatus.NOT_FOUND, getMessage("answerOption.question.not.found", dto.getQuestionId()));
                    });

            // Optional: Check if a answerOption with the same text already exists (if desired)
            boolean exists = answerOptionRepository.existsByQuestionIdAndText(dto.getQuestionId(), dto.getText());
            if (exists) {
                log.warn("AnswerOption already exists for Question ID {} with text: {}", dto.getQuestionId(), dto.getText());
                throw new AnswerOptionCreationException(HttpStatus.CONFLICT, getMessage("answerOption.text.already.exists", dto.getText()));
            }

            // Map DTO to entity
            AnswerOption answerOption = answerOptionMapper.toEntity(dto);
            answerOption.setQuestion(question);

            // Save entity
            AnswerOption savedAnswerOption = answerOptionRepository.save(answerOption);
            log.info("AnswerOption created successfully with ID: {}", savedAnswerOption.getId());

            return savedAnswerOption;

        } catch (AnswerOptionCreationException e) {
            throw e; // Already logged
        } catch (Exception e) {
            log.error("Unexpected error during answerOption creation: {}", e.getMessage(), e);
            throw new AnswerOptionCreationException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("answerOption.create.failed"));
        }
    }


    @Transactional
    public AnswerOption updateAnswerOption(Long id, AnswerOptionDTO dto) {
        if (id == null) {
            log.warn("Attempted to update answerOption with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("answerOption.id.empty"));
        }
        if (dto == null) {
            log.warn("AnswerOption update request cannot be null");
            throw new AnswerOptionUpdateException(HttpStatus.BAD_REQUEST, getMessage("answerOption.dto.empty"));
        }

        try {
            log.info("Updating answerOption with ID: {}", id);

            AnswerOption existingAnswerOption = getAnswerOptionById(id);
            if (existingAnswerOption == null) {
                log.warn("AnswerOption not found with ID: {}", id);
                throw new AnswerOptionUpdateException(HttpStatus.NOT_FOUND, getMessage("answerOption.not.found", id));
            }

            answerOptionMapper.updateAnswerOptionFromDto(dto, existingAnswerOption);

            AnswerOption savedAnswerOption = answerOptionRepository.save(existingAnswerOption);
            log.info("AnswerOption with ID {} successfully updated", savedAnswerOption.getId());
            return savedAnswerOption;

        } catch (Exception e) {
            log.error("Error updating answerOption with ID {}: {}", id, e.getMessage(), e);
            throw new AnswerOptionUpdateException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("answerOption.update.failed"));
        }
    }
}
