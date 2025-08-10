package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.StudentJobLink.StudentJobLinkCreationException;
import com.example.oriengo.exception.custom.StudentJobLink.StudentJobLinkDeleteException;
import com.example.oriengo.exception.custom.StudentJobLink.StudentJobLinkGetException;
import com.example.oriengo.exception.custom.StudentJobLink.StudentJobLinkUpdateException;
import com.example.oriengo.model.dto.StudentJobLinkRequestDto;
import com.example.oriengo.model.entity.StudentJobLink;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.model.entity.Job;
import com.example.oriengo.mapper.StudentJobLinkMapper;
import com.example.oriengo.repository.StudentJobLinkRepository;
import com.example.oriengo.repository.StudentRepository;
import com.example.oriengo.repository.JobRepository;
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
public class StudentJobLinkService {
    private final StudentJobLinkRepository repository;
    private final StudentRepository studentRepository;
    private final JobRepository jobRepository;
    private final StudentJobLinkMapper mapper;
    private final MessageSource messageSource; // Injected

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public List<StudentJobLink> findAll() {
        try {
            log.info("Fetching student job links");
            List<StudentJobLink> studentJobLinks = repository.findAll();
            log.info("Found {} student job links", studentJobLinks.size());
            return studentJobLinks;
        } catch (Exception e) {
            log.error("Failed to fetch student job links : {}", e.getMessage(), e);
            throw new StudentJobLinkGetException(HttpStatus.NOT_FOUND, getMessage("studentJobLinks.not.found"));
        }
    }

    public StudentJobLink findById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch student job link with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("studentJobLink.id.empty"));
        }

        log.info("Fetching student job link with ID: {}", id);

        return repository.findById(id)
            .orElseThrow(() -> {
                log.error("Student job link not found with ID: {}", id);
                return new StudentJobLinkGetException(HttpStatus.NOT_FOUND, getMessage("studentJobLink.not.found"));
            });
    }

    public StudentJobLink create(StudentJobLinkRequestDto requestDto) {
        if (requestDto == null) {
            log.warn("student job link request cannot be null");
            throw new StudentJobLinkCreationException(HttpStatus.BAD_REQUEST, getMessage("studentJobLink.dto.empty"));
        }
        try {
            log.info("Starting creation of new student job link");

            Student student = studentRepository.findById(requestDto.getStudentId())
                    .orElseThrow(() -> new StudentJobLinkCreationException(HttpStatus.NOT_FOUND, getMessage("studentJobLink.student.not.found")));

            Job job = jobRepository.findById(requestDto.getJobId())
                    .orElseThrow(() -> new StudentJobLinkCreationException(HttpStatus.NOT_FOUND, getMessage("studentJobLink.job.not.found")));

            StudentJobLink studentJobLink = mapper.toEntity(requestDto);
            studentJobLink.setStudent(student);
            studentJobLink.setJob(job);
            StudentJobLink savedStudentJobLink = repository.save(studentJobLink);
            log.info("Student job link created successfully with ID: {}", savedStudentJobLink.getId());
            return savedStudentJobLink;
        } catch (StudentJobLinkCreationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during student job link creation", e.getMessage(), e);
            throw new StudentJobLinkCreationException(HttpStatus.BAD_REQUEST, getMessage("studentJobLink.create.failed"));
        }
    }

    public StudentJobLink update(Long id, StudentJobLinkRequestDto requestDto) {
        if (id == null) {
            log.warn("Attempted to update student job link with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("studentJobLink.id.empty"));
        } else if (requestDto == null) {
            log.warn("student job link request cannot be null");
            throw new StudentJobLinkUpdateException(HttpStatus.BAD_REQUEST, getMessage("studentJobLink.dto.empty"));
        }
        try {
            StudentJobLink existingStudentJobLink = findById(id);

            if (requestDto.getStudentId() != null) {
                Student student = studentRepository.findById(requestDto.getStudentId())
                        .orElseThrow(() -> new StudentJobLinkUpdateException(HttpStatus.NOT_FOUND, getMessage("studentJobLink.student.not.found")));
                existingStudentJobLink.setStudent(student);
            }

            if (requestDto.getJobId() != null) {
                Job job = jobRepository.findById(requestDto.getJobId())
                        .orElseThrow(() -> new StudentJobLinkUpdateException(HttpStatus.NOT_FOUND, getMessage("studentJobLink.job.not.found")));
                existingStudentJobLink.setJob(job);
            }
            mapper.updateEntityFromDto(requestDto, existingStudentJobLink);
            StudentJobLink savedStudentJobLink = repository.save(existingStudentJobLink);
            log.info("Student job link updated successfully with ID: {}", savedStudentJobLink.getId());
            return savedStudentJobLink;
        } catch (Exception e) {
            log.error("Error updating Student job link with ID {}: {}", id, e.getMessage(), e);
            throw new StudentJobLinkUpdateException(HttpStatus.BAD_REQUEST, getMessage("studentJobLink.update.failed"));
        }

    }

    public void deleteById(Long id) {
        if (id == null) {
            log.warn("Attempted delete with null student job link ID");
            throw new StudentJobLinkDeleteException(HttpStatus.BAD_REQUEST, getMessage("studentJobLink.id.empty"));
        }
        try {
            log.info("Attempting delete for student job link with ID: {}", id);
            StudentJobLink studentJobLink = findById(id);
            repository.deleteById(studentJobLink.getId());
            log.info("Successfully deleted student job link with ID: {}", studentJobLink.getId());
        } catch (Exception e) {
            log.error("Error during delete of student job link with ID {}: {}", id, e.getMessage(), e);
            throw new StudentJobLinkDeleteException(HttpStatus.CONFLICT, getMessage("studentJobLink.delete.failed"));
        }
    }

    public List<StudentJobLink> findByStudentId(Long studentId) {
        if (studentId == null) {
            log.warn("Attempted to fetch student job link with null student ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("studentJobLink.id.empty"));
        }

        log.info("Fetching student job link with student ID: {}", studentId);

        return repository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    log.error("Student job link not found with student ID: {}", studentId);
                    return new StudentJobLinkGetException(HttpStatus.NOT_FOUND, getMessage("studentJobLink.not.found"));
                });
    }

    public List<StudentJobLink> findByJobId(Long jobId) {
        if (jobId == null) {
            log.warn("Attempted to fetch student job link with null job ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("studentJobLink.id.empty"));
        }

        log.info("Fetching student job link with job ID: {}", jobId);

        return repository.findByJobId(jobId)
                .orElseThrow(() -> {
                    log.error("Student job link not found with job ID: {}", jobId);
                    return new StudentJobLinkGetException(HttpStatus.NOT_FOUND, getMessage("studentJobLink.not.found"));
                });
    }

    public List<StudentJobLink> findByType(String type) {
        if (type == null) {
            log.warn("Attempted to fetch student job link with null job type");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("studentJobLink.id.empty"));
        }

        log.info("Fetching student job link with job type: {}", type);

        return repository.findByType(type)
                .orElseThrow(() -> {
                    log.error("Student job link not found with job type: {}", type);
                    return new StudentJobLinkGetException(HttpStatus.NOT_FOUND, getMessage("studentJobLink.not.found"));
                });
    }
} 