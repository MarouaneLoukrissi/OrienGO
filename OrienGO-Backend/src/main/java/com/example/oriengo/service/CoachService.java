package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.user.*;
import com.example.oriengo.mapper.CoachMapper;
import com.example.oriengo.model.dto.CoachCreateDTO;
import com.example.oriengo.model.dto.CoachUpdateProfileDTO;
import com.example.oriengo.model.entity.Coach;
import com.example.oriengo.model.entity.Role;
import com.example.oriengo.repository.CoachRepository;
import com.example.oriengo.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class CoachService {
    private final CoachRepository coachRepository;
    private final CoachMapper coachMapper;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;

    private String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
    }

    public List<Coach> getCoaches(boolean deleted) {
        try {
            log.info("Fetching coaches with isDeleted = {}", deleted);
            List<Coach> coaches = coachRepository.findByIsDeleted(deleted);
            log.info("Found {} coaches", coaches.size());
            return coaches;
        } catch (Exception e) {
            log.error("Failed to fetch coaches: {}", e.getMessage(), e);
            throw new UserGetException(HttpStatus.NOT_FOUND, getMessage("coach.not.found"));
        }
    }

    public Coach getCoachById(Long id, boolean deleted) {
        if (id == null) {
            log.warn("Attempted to fetch coach with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("coach.id.empty"));
        }
        return coachRepository.findByIdAndIsDeleted(id, deleted)
                .orElseThrow(() -> {
                    log.error("Coach not found with ID: {}", id);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("coach.not.found"));
                });
    }

    public Coach getCoachById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch coach with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("coach.id.empty"));
        }
        return coachRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Coach not found with ID: {}", id);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("coach.not.found"));
                });
    }

    public Coach getCoachByEmail(String email, boolean deleted) {
        if (email == null || email.trim().isEmpty()) {
            log.warn("Attempted to fetch coach with null or empty email");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("coach.email.empty"));
        }
        return coachRepository.findByEmailAndIsDeleted(email, deleted)
                .orElseThrow(() -> {
                    log.error("Coach not found with email: {}", email);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("coach.not.found"));
                });
    }

    @Transactional
    public void hardDeleteCoach(Long id) {
        if (id == null) {
            log.warn("Attempted hard delete with null coach ID");
            throw new UserDeleteException(HttpStatus.BAD_REQUEST, getMessage("coach.id.empty"));
        }
        try {
            Coach coach = getCoachById(id);
            coachRepository.deleteById(coach.getId());
            log.info("Successfully hard deleted coach with ID: {}", coach.getId());
        } catch (Exception e) {
            log.error("Error during hard delete of coach with ID {}: {}", id, e.getMessage(), e);
            throw new UserDeleteException(HttpStatus.CONFLICT, getMessage("coach.hard.delete.failed"));
        }
    }

    @Transactional
    public void softDeleteCoach(Long id) {
        if (id == null) {
            log.warn("Attempted soft delete with null coach ID");
            throw new UserDeleteException(HttpStatus.BAD_REQUEST, getMessage("coach.id.empty"));
        }
        try {
            Coach coach = getCoachById(id, false);
            String originalEmail = coach.getEmail();

            coach.setDeleted(true);
            coach.setDeletedAt(LocalDateTime.now());
            coach.setEmail("deleted_" + UUID.randomUUID() + "_" + originalEmail);

            coachRepository.save(coach);

            log.info("Successfully soft deleted coach with ID: {}", coach.getId());
        } catch (Exception e) {
            log.error("Error during soft delete of coach with ID {}: {}", id, e.getMessage(), e);
            throw new UserDeleteException(HttpStatus.CONFLICT, getMessage("coach.soft.delete.failed"));
        }
    }

    @Transactional
    public Coach restoreCoach(Long id) {
        if (id == null) {
            log.warn("Attempted restore with null coach ID");
            throw new UserRestoreException(HttpStatus.BAD_REQUEST, getMessage("coach.id.empty"));
        }
        try {
            log.info("Attempting restore for coach with ID: {}", id);

            Coach coach = getCoachById(id, true);
            String deletedEmail = coach.getEmail();

            // Extract original email from deleted email format "deleted_<UUID>_originalEmail"
            String prefix = "deleted_";
            int index = deletedEmail.indexOf("_", prefix.length()); // find second underscore
            if (index == -1 || index + 1 >= deletedEmail.length()) {
                throw new UserRestoreException(HttpStatus.CONFLICT, getMessage("coach.restore.email.invalid"));
            }

            String originalEmail = deletedEmail.substring(index + 1);

            // Check if original email is already used by non-deleted coach
            boolean emailTaken = coachRepository.existsByEmailAndIsDeletedFalse(originalEmail);
            if (emailTaken) {
                log.warn("Original email {} is already in use. Cannot restore to original email.", originalEmail);
                throw new UserRestoreException(HttpStatus.CONFLICT, getMessage("coach.restore.email.used"));
            }

            coach.setDeleted(false);
            coach.setDeletedAt(null);
            coach.setEmail(originalEmail);

            coachRepository.save(coach);

            log.info("Successfully restored coach with ID: {}", coach.getId());
            return coach;
        } catch (Exception e) {
            log.error("Error during restore of coach with ID {}: {}", id, e.getMessage(), e);
            throw new UserRestoreException(HttpStatus.BAD_REQUEST, getMessage("coach.restore.failed"));
        }
    }

    @Transactional
    public Coach createCoach(CoachCreateDTO dto) {
        if (dto == null) {
            log.warn("Coach request cannot be null");
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("coach.dto.empty"));
        }
        try {
            log.info("Starting creation of new coach with email: {}", dto.getEmail());

            // Check if email already exists
            try {
                Coach existingCoach = getCoachByEmail(dto.getEmail(), false);
                if (existingCoach != null) {
                    log.warn("Coach already exists with email: {}", dto.getEmail());
                    throw new UserCreationException(HttpStatus.CONFLICT, getMessage("coach.email.already.exists", dto.getEmail()));
                }
            } catch (UserGetException e) {
                log.debug("No existing coach found with email {}, proceeding with creation", dto.getEmail());
            }

            Coach coach = coachMapper.toEntity(dto);
            coach.setPassword(coach.getPassword()); // encoder.encode(coach.getPassword())
            coach.setEnabled(true);

            String roleName = "COACH";
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> {
                        log.warn("Role '{}' not found in database", roleName);
                        return new UserCreationException(HttpStatus.NOT_FOUND , getMessage("coach.role.not.found", roleName));
                    });

            coach.setRoles(Set.of(role));

            Coach savedCoach = coachRepository.save(coach);
            log.info("Coach created successfully with ID: {}", savedCoach.getId());

            return savedCoach;

        } catch (UserCreationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during coach creation for email {}: {}", dto.getEmail(), e.getMessage(), e);
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("coach.create.failed"));
        }
    }

    @Transactional
    public Coach updateCoach(Long id, CoachCreateDTO dto) {
        if (id == null) {
            log.warn("Attempted to update coach with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("coach.id.empty"));
        }
        if (dto == null) {
            log.warn("Coach update request cannot be null");
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("coach.dto.empty"));
        }
        try {
            log.info("Updating coach with ID: {}", id);
            Coach coach = coachMapper.toEntity(dto);
            Coach existingCoach = getCoachById(id);
            coach.setId(existingCoach.getId());

            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                coach.setPassword(coach.getPassword()); // encoder.encode(coach.getPassword())
            }

            Coach savedCoach = coachRepository.save(coach);
            log.info("Coach with ID {} successfully updated", savedCoach.getId());
            return savedCoach;
        } catch (Exception e) {
            log.error("Error updating coach with ID {}: {}", id, e.getMessage(), e);
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("coach.update.failed"));
        }
    }
    @Transactional
    public Coach updateProfileCoach(Long id, CoachUpdateProfileDTO dto) {
        if (id == null) {
            log.warn("Attempted to update coach with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("coach.id.empty"));
        }
        if (dto == null) {
            log.warn("Coach update request cannot be null");
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("coach.dto.empty"));
        }
        try {
            log.info("Updating coach with ID: {}", id);
            Coach existingCoach = getCoachById(id, false);
            coachMapper.updateCoachFromDto(dto, existingCoach);
            Coach savedCoach = coachRepository.save(existingCoach);
            log.info("Coach with ID {} successfully updated", savedCoach.getId());
            return savedCoach;
        } catch (Exception e) {
            log.error("Error updating coach with ID {}: {}", id, e.getMessage(), e);
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("coach.update.failed"));
        }
    }
}
