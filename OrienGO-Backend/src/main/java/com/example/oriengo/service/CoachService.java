package com.example.oriengo.service;

import com.example.oriengo.exception.PathVarException;
import com.example.oriengo.exception.user.UserCreationException;
import com.example.oriengo.exception.user.UserDeleteException;
import com.example.oriengo.exception.user.UserGetException;
import com.example.oriengo.exception.user.UserUpdateException;
import com.example.oriengo.mapper.CoachMapper;
import com.example.oriengo.model.dto.CoachCreateDTO;
import com.example.oriengo.model.entity.Coach;
import com.example.oriengo.repository.CoachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class CoachService {
    private final CoachRepository coachRepository;
    private final CoachMapper coachMapper;

    public List<Coach> getCoachs(boolean deleted) {
        try{
            return coachRepository.findByIsDeleted(deleted);
        } catch (Exception e){
            throw new UserGetException(HttpStatus.NOT_FOUND, "No Coach found");
        }
    }

    public Coach getCoachById(Long id, boolean deleted) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Coach ID cannot be empty");
        }
        return coachRepository.findByIdAndIsDeleted(id, deleted)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "Coach not found"));
    }

    public Coach getCoachById(Long id) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Coach ID cannot be empty");
        }
        return coachRepository.findById(id)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "Coach not found"));
    }

    public Coach getCoachByEmail(String email, boolean deleted) {
        if (email == null || email.trim().isEmpty()) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Coach Email cannot be empty");
        }
        return coachRepository.findByEmailAndIsDeleted(email, deleted)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "Coach not found"));
    }

    public void hardDeleteCoach(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coach ID cannot be empty");
        }
        try{
            Coach coach = getCoachById(id);
            coachRepository.deleteById(coach.getId());
            log.info("Coach hard deleted with ID: {}", coach.getId());
        } catch (Exception e) {
            log.error("Error hard deleting coach: {}", e.getMessage());
            throw new UserDeleteException("Failed to hard delete coach");
        }
    }

    public void softDeleteCoach(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coach ID cannot be empty");
        }
        try{
            Coach coach = getCoachById(id, false);
            coach.setDeleted(true);
            coach.setDeletedAt(LocalDateTime.now());
            coach.setEmail("deleted_" + UUID.randomUUID() + "_" + coach.getEmail());
            coachRepository.save(coach);
            log.info("Coach soft deleted with ID: {}", coach.getId());
        } catch (Exception e) {
            log.error("Error soft deleting coach: {}", e.getMessage());
            throw new UserDeleteException("Failed to soft delete coach");
        }
    }

    public Coach createCoach(CoachCreateDTO dto) {
        try {
            Coach coach = coachMapper.toEntity(dto);
            coach.setPassword(coach.getPassword()); //encoder.encode(coach.getPassword())
            Coach coachOutput = coachRepository.save(coach);
            log.info("Coach created with ID: {}", coachOutput.getId());
            return coachOutput;
        } catch (Exception e) {
            log.error("Error creating coach: {}", e.getMessage());
            throw new UserCreationException("Failed to create coach");
        }
    }

    public Coach updateCoach(Long id, CoachCreateDTO dto) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Coach ID cannot be empty");
        }
        try {
            Coach coach = coachMapper.toEntity(dto);
            Coach coachChecked = getCoachById(id);
            coach.setId(coachChecked.getId());
            coach.setPassword(coach.getPassword()); //encoder.encode(coach.getPassword())
            Coach coachOutput = coachRepository.save(coach);
            log.info("Coach updated with ID: {}", coachOutput.getId());
            return coachOutput;
        } catch (Exception e) {
            log.error("Error updating coach: {}", e.getMessage());
            throw new UserUpdateException("Failed to update coach");
        }
    }
}
