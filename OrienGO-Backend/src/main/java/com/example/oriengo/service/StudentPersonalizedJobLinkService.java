package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.StudentPersonalizedJobLink.StudentPersonalizedJobLinkCreationException;
import com.example.oriengo.exception.custom.StudentPersonalizedJobLink.StudentPersonalizedJobLinkDeleteException;
import com.example.oriengo.exception.custom.StudentPersonalizedJobLink.StudentPersonalizedJobLinkGetException;
import com.example.oriengo.mapper.StudentPersonalizedJobLinkMapper;
import com.example.oriengo.model.dto.StudentPersonalizedJobLinkRequestDto;
import com.example.oriengo.model.entity.PersonalizedJob;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.model.entity.StudentPersonalizedJobLink;
import com.example.oriengo.repository.PersonalizedJobRepository;
import com.example.oriengo.repository.StudentPersonalizedJobLinkRepository;
import com.example.oriengo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudentPersonalizedJobLinkService {

    private final StudentPersonalizedJobLinkRepository repository;
    private final StudentRepository studentRepository;
    private final PersonalizedJobRepository personalizedJobRepository;
    private final StudentPersonalizedJobLinkMapper mapper;
    private final MessageSource messageSource;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public List<StudentPersonalizedJobLink> findAll() {
        try {
            log.info("Fetching all student personalized job links");
            List<StudentPersonalizedJobLink> links = repository.findAll();
            log.info("Found {} student personalized job links", links.size());
            return links;
        } catch (Exception e) {
            log.error("Failed to fetch student personalized job links: {}", e.getMessage(), e);
            throw new StudentPersonalizedJobLinkGetException(HttpStatus.NOT_FOUND, getMessage("studentPersonalizedJobLink.not.found"));
        }
    }

    public StudentPersonalizedJobLink findById(Long id) {
        if (id == null) {
            log.warn("Student personalized job link ID is null");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("studentPersonalizedJobLink.id.empty"));
        }

        log.info("Fetching student personalized job link with ID: {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("student personalized job link not found with ID: {}", id);
                    return new StudentPersonalizedJobLinkGetException(HttpStatus.NOT_FOUND, getMessage("studentPersonalizedJobLink.not.found"));
                });
    }

    public StudentPersonalizedJobLink save(StudentPersonalizedJobLinkRequestDto dto) {
        if (dto == null) {
            log.warn("Student personalized job link DTO is null");
            throw new StudentPersonalizedJobLinkCreationException(HttpStatus.BAD_REQUEST, getMessage("studentPersonalizedJobLink.dto.empty"));
        }
        try {
            Student student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new StudentPersonalizedJobLinkCreationException(HttpStatus.BAD_REQUEST, getMessage("student.not.found")));

            PersonalizedJob personalizedJob = personalizedJobRepository.findById(dto.getPersonalizedJobId())
                    .orElseThrow(() -> new StudentPersonalizedJobLinkCreationException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.not.found")));

            StudentPersonalizedJobLink link = mapper.toEntity(dto);
            link.setStudent(student);
            link.setPersonalizedJob(personalizedJob);

            StudentPersonalizedJobLink saved = repository.save(link);
            log.info("Saved student personalized job link with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Error saving student personalized job link: {}", e.getMessage(), e);
            throw new StudentPersonalizedJobLinkCreationException(HttpStatus.BAD_REQUEST, getMessage("studentPersonalizedJobLink.create.failed"));
        }
    }

    public void deleteById(Long id) {
        if (id == null) {
            log.warn("Student personalized job link ID is null for deletion");
            throw new StudentPersonalizedJobLinkDeleteException(HttpStatus.BAD_REQUEST, getMessage("studentPersonalizedJobLink.id.empty"));
        }

        try {
            log.info("Attempting delete for student personalized job link with ID: {}", id);
            StudentPersonalizedJobLink existing = findById(id);
            repository.deleteById(existing.getId());
            log.info("Deleted student personalized job link with ID: {}", existing.getId());
        } catch (Exception e) {
            log.error("Error deleting student personalized job link with ID {}: {}", id, e.getMessage(), e);
            throw new StudentPersonalizedJobLinkDeleteException(HttpStatus.CONFLICT, getMessage("studentPersonalizedJobLink.delete.failed"));
        }
    }
}
