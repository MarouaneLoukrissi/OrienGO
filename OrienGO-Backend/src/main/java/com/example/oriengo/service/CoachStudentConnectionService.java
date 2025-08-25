package com.example.oriengo.service;

import com.example.oriengo.exception.custom.CoachStudentConnections.CoachStudentConnectionCreationException;
import com.example.oriengo.exception.custom.CoachStudentConnections.CoachStudentConnectionDeleteException;
import com.example.oriengo.exception.custom.CoachStudentConnections.CoachStudentConnectionGetException;
import com.example.oriengo.exception.custom.CoachStudentConnections.CoachStudentConnectionUpdateException;
import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.mapper.CoachStudentConnectionMapper;
import com.example.oriengo.model.dto.*;
import com.example.oriengo.model.entity.Coach;
import com.example.oriengo.model.entity.CoachStudentConnection;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.model.enumeration.Category;
import com.example.oriengo.model.enumeration.ConnectionStatus;
import com.example.oriengo.model.enumeration.RequestInitiator;
import com.example.oriengo.repository.CoachStudentConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CoachStudentConnectionService {
    private final CoachStudentConnectionRepository repository;
    private final CoachStudentConnectionMapper coachStudentConnectionMapper;
    private final MessageSource messageSource;
    private final StudentService studentService;
    private final CoachService coachService;
    private final TestResultService testResultService;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public List<CoachStudentConnection> findAll() {
        try {
            log.info("Fetching coach student connections");
            List<CoachStudentConnection> coachStudentConnections = repository.findAll();
            log.info("Found {} coach student connections", coachStudentConnections.size());
            return coachStudentConnections;
        } catch (Exception e) {
            log.error("Failed to fetch coach student connections : {}", e.getMessage(), e);
            throw new CoachStudentConnectionGetException(HttpStatus.NOT_FOUND, getMessage("coachStudentConnection.not.found"));
        }
    }

    public CoachStudentConnection findById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch coach student connection with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("coachStudentConnection.id.empty"));
        }

        log.info("Fetching coach student connection with ID: {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("CoachStudentConnection not found with ID: {}", id);
                    return new CoachStudentConnectionGetException(HttpStatus.NOT_FOUND, getMessage("coachStudentConnection.not.found"));
                });
    }

    public CoachStudentConnection create(CoachStudentConnectionCreateDTO requestDto) {
        if (requestDto == null) {
            log.warn(getMessage("coachStudentConnection.dto.null")); // log message also i18n
            throw new CoachStudentConnectionCreationException(
                    HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.dto.empty")
            );
        }

        try {
            log.info(getMessage("coachStudentConnection.creation.starting"));

            Student student = studentService.getStudentById(requestDto.getStudentId(), false);
            Coach coach = coachService.getCoachById(requestDto.getCoachId(), false);

            // Check if connection already exists
            boolean exists = repository.existsByCoachAndStudent(coach, student);
            if (exists) {
                log.warn(getMessage("coachStudentConnection.already.exists.log", coach.getId(), student.getId()));
                throw new CoachStudentConnectionCreationException(
                        HttpStatus.CONFLICT,
                        getMessage("coachStudentConnection.already.exists", coach.getId(), student.getId())
                );
            }

            CoachStudentConnection coachStudentConnection = new CoachStudentConnection();
            coachStudentConnection.setCoach(coach);
            coachStudentConnection.setStudent(student);

            coachStudentConnection.setRequestedBy(requestDto.getRequestedBy());

            CoachStudentConnection savedCoachStudentConnection = repository.saveAndFlush(coachStudentConnection);

            log.info(getMessage("coachStudentConnection.created.success", savedCoachStudentConnection.getId(), savedCoachStudentConnection.getRequestedAt()));

            return savedCoachStudentConnection;
        } catch (Exception e) {
            log.error(getMessage("coachStudentConnection.creation.failed"), e);
            throw new CoachStudentConnectionCreationException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    getMessage("coachStudentConnection.creation.failed") + ": " + e.getMessage()
            );
        }
    }


    public CoachStudentConnection update(Long id, CoachStudentConnectionUpdateDTO requestDto) {
        if (id == null) {
            log.warn(getMessage("coachStudentConnection.id.empty"));
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("coachStudentConnection.id.empty"));
        } else if (requestDto == null) {
            log.warn(getMessage("coachStudentConnection.dto.null"));
            throw new CoachStudentConnectionUpdateException(HttpStatus.BAD_REQUEST, getMessage("coachStudentConnection.dto.empty"));
        }
        try {
            CoachStudentConnection existingEntity = findById(id);

            Student student = studentService.getStudentById(requestDto.getStudentId(), false);
            Coach coach = coachService.getCoachById(requestDto.getCoachId(), false);
            existingEntity.setCoach(coach);
            existingEntity.setStudent(student);

            // ✅ Update initiator if provided
            if (requestDto.getRequestedBy() != null) {
                existingEntity.setRequestedBy(requestDto.getRequestedBy());
            }

            coachStudentConnectionMapper.updateEntityFromDto(requestDto, existingEntity);
            CoachStudentConnection savedCoachStudentConnection = repository.save(existingEntity);

            log.info(getMessage("coachStudentConnection.updated.success", savedCoachStudentConnection.getId()));
            return savedCoachStudentConnection;
        } catch (Exception e) {
            log.error(getMessage("coachStudentConnection.update.failed"), e);
            throw new CoachStudentConnectionUpdateException(HttpStatus.INTERNAL_SERVER_ERROR,
                    getMessage("coachStudentConnection.update.failed") + ": " + e.getMessage());
        }
    }

    public void deleteById(Long id) {
        if (id == null) {
            log.warn("Attempted hard delete with null coachStudentConnection ID");
            throw new CoachStudentConnectionDeleteException(HttpStatus.BAD_REQUEST, getMessage("coachStudentConnection.id.empty"));
        }
        try {
            log.info("Attempting delete for coachStudentConnection with ID: {}", id);
            CoachStudentConnection coachStudentConnection = findById(id);
            repository.deleteById(coachStudentConnection.getId());
            log.info("Successfully deleted coachStudentConnection with ID: {}", coachStudentConnection.getId());
        } catch (Exception e) {
            log.error("Error during delete of coachStudentConnection with ID {}: {}", id, e.getMessage(), e);
            throw new CoachStudentConnectionDeleteException(HttpStatus.CONFLICT, getMessage("coachStudentConnection.delete.failed"));
        }
    }

    public List<CoachStudentConnection> findByCoachIdAndStatusAndRequestedBy(Long coachId, String status, String requestedBy) {
        if (coachId == null) {
            log.warn("Attempted to fetch coachStudentConnection with null coach Id");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.coach.id.null"));
        }
        if (status == null) {
            log.warn("Attempted to fetch coachStudentConnection with null status");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.status.null"));
        }
        if (requestedBy == null) {
            log.warn("Attempted to fetch coachStudentConnection with null requestedBy");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.requestedBy.null"));
        }

        try {
            // ✅ Convert status to enum
            ConnectionStatus connectionStatus = ConnectionStatus.valueOf(status.toUpperCase());

            // ✅ Convert requestedBy to enum
            RequestInitiator requestedByEnum = RequestInitiator.valueOf(requestedBy.toUpperCase());

            // ✅ Query with both enums
            return repository.findByCoachIdAndStatusAndRequestedBy(coachId, connectionStatus, requestedByEnum);

        } catch (IllegalArgumentException e) {
            String validStatuses = Arrays.stream(ConnectionStatus.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String validRequestedBy = Arrays.stream(RequestInitiator.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String message = getMessage("coachStudentConnection.enum.invalid",
                    status.toUpperCase(), validStatuses,
                    requestedBy.toUpperCase(), validRequestedBy);

            throw new CoachStudentConnectionGetException(HttpStatus.BAD_REQUEST, message);

        } catch (Exception e) {
            log.error("Failed to fetch coachStudentConnection : {}", e.getMessage(), e);
            throw new CoachStudentConnectionGetException(HttpStatus.NOT_FOUND,
                    getMessage("coachStudentConnection.not.found"));
        }
    }

    public List<CoachStudentConnection> findByStudentIdAndStatusAndRequestedBy(Long studentId, String status, String requestedBy) {
        if (studentId == null) {
            log.warn("Attempted to fetch coachStudentConnection with null student Id");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.student.id.null"));
        }
        if (status == null) {
            log.warn("Attempted to fetch coachStudentConnection with null status");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.status.null"));
        }
        if (requestedBy == null) {
            log.warn("Attempted to fetch coachStudentConnection with null requestedBy");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.requestedBy.null"));
        }

        try {
            // ✅ Convert status to enum
            ConnectionStatus connectionStatus = ConnectionStatus.valueOf(status.toUpperCase());

            // ✅ Convert requestedBy to enum
            RequestInitiator requestedByEnum = RequestInitiator.valueOf(requestedBy.toUpperCase());

            return repository.findByStudentIdAndStatusAndRequestedBy(studentId, connectionStatus, requestedByEnum);

        } catch (IllegalArgumentException e) {
            String validStatuses = Arrays.stream(ConnectionStatus.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String validRequestedBy = Arrays.stream(RequestInitiator.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String message = getMessage("coachStudentConnection.enum.invalid",
                    status.toUpperCase(), validStatuses,
                    requestedBy.toUpperCase(), validRequestedBy);

            throw new CoachStudentConnectionGetException(HttpStatus.BAD_REQUEST, message);

        } catch (Exception e) {
            log.error("Failed to fetch coachStudentConnection : {}", e.getMessage(), e);
            throw new CoachStudentConnectionGetException(HttpStatus.NOT_FOUND,
                    getMessage("coachStudentConnection.not.found"));
        }
    }
    public long countByStudentIdAndStatus(Long studentId, String status) {
        if (studentId == null) {
            log.warn("Attempted to count coachStudentConnection with null student Id");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.student.id.null"));
        }
        if (status == null) {
            log.warn("Attempted to count coachStudentConnection with null status");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.status.null"));
        }

        try {
            // Convert status to enum
            ConnectionStatus connectionStatus = ConnectionStatus.valueOf(status.toUpperCase());

            // ✅ Return count instead of list
            return repository.countByStudentIdAndStatusAndRequestedBy(studentId, connectionStatus);

        } catch (IllegalArgumentException e) {
            String validStatuses = Arrays.stream(ConnectionStatus.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String validRequestedBy = Arrays.stream(RequestInitiator.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String message = getMessage("coachStudentConnection.enum.invalid",
                    status.toUpperCase(), validStatuses, validRequestedBy);

            throw new CoachStudentConnectionGetException(HttpStatus.BAD_REQUEST, message);

        } catch (Exception e) {
            log.error("Failed to count coachStudentConnection : {}", e.getMessage(), e);
            throw new CoachStudentConnectionGetException(HttpStatus.NOT_FOUND,
                    getMessage("coachStudentConnection.not.found"));
        }
    }

    public long countByCoachIdAndStatus(Long coachId, String status) {
        if (coachId == null) {
            log.warn("Attempted to count coachStudentConnection with null coach Id");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.coach.id.null"));
        }
        if (status == null) {
            log.warn("Attempted to count coachStudentConnection with null status");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.status.null"));
        }

        try {
            ConnectionStatus connectionStatus = ConnectionStatus.valueOf(status.toUpperCase());

            return repository.countByCoachIdAndStatusAndRequestedBy(coachId, connectionStatus);

        } catch (IllegalArgumentException e) {
            String validStatuses = Arrays.stream(ConnectionStatus.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String validRequestedBy = Arrays.stream(RequestInitiator.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String message = getMessage("coachStudentConnection.enum.invalid",
                    status.toUpperCase(), validStatuses, validRequestedBy);

            throw new CoachStudentConnectionGetException(HttpStatus.BAD_REQUEST, message);

        } catch (Exception e) {
            log.error("Failed to count coachStudentConnection : {}", e.getMessage(), e);
            throw new CoachStudentConnectionGetException(HttpStatus.NOT_FOUND,
                    getMessage("coachStudentConnection.not.found"));
        }
    }

    @Transactional(readOnly = true)
    public TestResultAverageDTO getDominantProfileByCoachIdStatus(
            Long coachId, String status) {

        if (coachId == null) {
            log.warn("Attempted to fetch students with null coach Id");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.coach.id.null"));
        }
        if (status == null) {
            log.warn("Attempted to fetch students with null status");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.status.null"));
        }

        try {
            ConnectionStatus connectionStatus = ConnectionStatus.valueOf(status.toUpperCase());

            // Fetch all student IDs for this coach + status + requestedBy
            List<Long> studentIds = repository.findStudentIdsByCoachIdAndStatusAndRequestedBy(
                    coachId, connectionStatus
            );

            if (studentIds.isEmpty()) {
                log.warn("No students found for coachId {}, status {}, requestedBy {}", coachId, status);
                return TestResultAverageDTO.builder()
                        .dominantProfile(null)
                        .percentage(0.0)
                        .build();
            }

            // Use existing method to calculate dominant profile
            return testResultService.getAverageByStudentIds(studentIds);

        } catch (IllegalArgumentException e) {
            String validStatuses = Arrays.stream(ConnectionStatus.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String validRequestedBy = Arrays.stream(RequestInitiator.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String message = getMessage("coachStudentConnection.enum.invalid",
                    status.toUpperCase(), validStatuses, validRequestedBy);

            throw new CoachStudentConnectionGetException(HttpStatus.BAD_REQUEST, message);

        } catch (Exception e) {
            log.error("Failed to get dominant profile for coachStudentConnection: {}", e.getMessage(), e);
            throw new CoachStudentConnectionGetException(HttpStatus.NOT_FOUND,
                    getMessage("coachStudentConnection.not.found"));
        }
    }
    @Transactional(readOnly = true)
    public TestResultProfilesDTO getAverageProfilesByCoachIdStatus(Long coachId, String status) {
        if (coachId == null) {
            log.warn("Attempted to fetch students with null coach Id");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.coach.id.null"));
        }
        if (status == null) {
            log.warn("Attempted to fetch students with null status");
            throw new PathVarException(HttpStatus.BAD_REQUEST,
                    getMessage("coachStudentConnection.status.null"));
        }

        try {
            ConnectionStatus connectionStatus = ConnectionStatus.valueOf(status.toUpperCase());

            // Fetch all student IDs for this coach + status
            List<Long> studentIds = repository.findStudentIdsByCoachIdAndStatusAndRequestedBy(coachId, connectionStatus);

            if (studentIds.isEmpty()) {
                log.warn("No students found for coachId {}, status {}", coachId, status);

                // Return all categories with 0.0 if no students
                List<ProfileScoreDTO> emptyProfiles = Arrays.stream(Category.values())
                        .map(cat -> new ProfileScoreDTO(cat, 0.0))
                        .collect(Collectors.toList());

                return TestResultProfilesDTO.builder()
                        .profiles(emptyProfiles)
                        .build();
            }

            // Use existing method to calculate average profiles across students
            return testResultService.getAverageProfilesByStudentIds(studentIds);

        } catch (IllegalArgumentException e) {
            String validStatuses = Arrays.stream(ConnectionStatus.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String message = getMessage("coachStudentConnection.enum.invalid",
                    status.toUpperCase(), validStatuses);

            throw new CoachStudentConnectionGetException(HttpStatus.BAD_REQUEST, message);

        } catch (Exception e) {
            log.error("Failed to get average profiles for coachStudentConnection: {}", e.getMessage(), e);
            throw new CoachStudentConnectionGetException(HttpStatus.NOT_FOUND,
                    getMessage("coachStudentConnection.not.found"));
        }
    }
}
