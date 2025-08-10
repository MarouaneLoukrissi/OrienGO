package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.Privilege.PrivilegeCreationException;
import com.example.oriengo.exception.custom.Privilege.PrivilegeDeleteException;
import com.example.oriengo.exception.custom.Privilege.PrivilegeGetException;
import com.example.oriengo.exception.custom.Privilege.PrivilegeUpdateException;
import com.example.oriengo.mapper.PrivilegeMapper;
import com.example.oriengo.model.dto.PrivilegeCreateDTO;
import com.example.oriengo.model.entity.Privilege;

import com.example.oriengo.repository.PrivilegeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
@Slf4j
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeMapper privilegeMapper;
    private final MessageSource messageSource;

    private String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
    }

    public List<Privilege> getPrivileges() {
        try {
            log.info("Fetching all privileges");
            List<Privilege> privileges = privilegeRepository.findAll();
            log.info("Found {} privileges", privileges.size());
            return privileges;
        } catch (Exception e) {
            log.error("Failed to fetch privileges: {}", e.getMessage(), e);
            throw new PrivilegeGetException(HttpStatus.NOT_FOUND, getMessage("privilege.not.found"));
        }
    }

    public Privilege getPrivilegeById(Long id) {
        if (id == null) {
            log.warn("Privilege ID is null");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("privilege.id.empty"));
        }
        return privilegeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Privilege not found with ID: {}", id);
                    return new PrivilegeGetException(HttpStatus.NOT_FOUND, getMessage("privilege.not.found"));
                });
    }

    public Privilege getPrivilegeByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.warn("Privilege name is null or empty");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("privilege.name.empty"));
        }
        return privilegeRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("Privilege not found with name: {}", name);
                    return new PrivilegeGetException(HttpStatus.NOT_FOUND, getMessage("privilege.not.found"));
                });
    }

    @Transactional
    public void deletePrivilege(Long id) {
        if (id == null) {
            log.warn("Privilege ID is null for deletion");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("privilege.id.empty"));
        }
        try {
            Privilege privilege = getPrivilegeById(id);
            privilegeRepository.deleteById(privilege.getId());
            log.info("Privilege deleted with ID: {}", privilege.getId());
        } catch (Exception e) {
            log.error("Error deleting privilege with ID {}: {}", id, e.getMessage(), e);
            throw new PrivilegeDeleteException(HttpStatus.CONFLICT, getMessage("privilege.delete.failed"));
        }
    }

    @Transactional
    public Privilege createPrivilege(PrivilegeCreateDTO dto) {
        if (dto == null) {
            log.warn("PrivilegeCreateDTO is null");
            throw new PrivilegeCreationException(HttpStatus.BAD_REQUEST, getMessage("privilege.dto.empty"));
        }
        try {
            Privilege privilege = privilegeMapper.toEntity(dto);
            Privilege savedPrivilege = privilegeRepository.save(privilege);
            log.info("Privilege created with ID: {}", savedPrivilege.getId());
            return savedPrivilege;
        } catch (PrivilegeCreationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating privilege: {}", e.getMessage(), e);
            throw new PrivilegeCreationException(HttpStatus.BAD_REQUEST, getMessage("privilege.create.failed"));
        }
    }

    @Transactional
    public Privilege updatePrivilege(Long id, PrivilegeCreateDTO dto) {
        if (id == null) {
            log.warn("Privilege ID is null for update");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("privilege.id.empty"));
        }
        if (dto == null) {
            log.warn("PrivilegeCreateDTO is null for update");
            throw new PrivilegeUpdateException(HttpStatus.BAD_REQUEST, getMessage("privilege.dto.empty"));
        }
        try {
            Privilege existingPrivilege = getPrivilegeById(id);
            Privilege privilegeToUpdate = privilegeMapper.toEntity(dto);
            privilegeToUpdate.setId(existingPrivilege.getId());
            Privilege updatedPrivilege = privilegeRepository.save(privilegeToUpdate);
            log.info("Privilege updated with ID: {}", updatedPrivilege.getId());
            return updatedPrivilege;
        } catch (Exception e) {
            log.error("Error updating privilege with ID {}: {}", id, e.getMessage(), e);
            throw new PrivilegeUpdateException(HttpStatus.BAD_REQUEST, getMessage("privilege.update.failed"));
        }
    }
}
