package com.example.oriengo.service;

import com.example.oriengo.exception.custom.Role.RoleUpdateException;
import com.example.oriengo.exception.custom.Role.RoleCreationException;
import com.example.oriengo.exception.custom.Role.RoleDeleteException;
import com.example.oriengo.exception.custom.Role.RoleGetException;
import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.mapper.RoleMapper;
import com.example.oriengo.model.dto.RoleCreateDTO;
import com.example.oriengo.model.entity.Role;
import com.example.oriengo.repository.PrivilegeRepository;
import com.example.oriengo.repository.RoleRepository;
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
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final MessageSource messageSource;
    private final PrivilegeRepository privilegeRepository;



    private String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
    }

    public List<Role> getRoles() {
        try {
            log.info("Fetching all roles");
            List<Role> roles = roleRepository.findAll();
            log.info("Found {} roles", roles.size());
            return roles;
        } catch (Exception e) {
            log.error("Failed to fetch roles: {}", e.getMessage(), e);
            throw new RoleGetException(HttpStatus.NOT_FOUND, getMessage("role.not.found"));
        }
    }

    public Role getRoleById(Long id) {
        if (id == null) {
            log.warn("Role ID is null");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("role.id.empty"));
        }
        return roleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Role not found with ID: {}", id);
                    return new RoleGetException(HttpStatus.NOT_FOUND, getMessage("role.not.found"));
                });
    }

    @Transactional
    public void deleteRole(Long id) {
        if (id == null) {
            log.warn("Role ID is null for deletion");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("role.id.empty"));
        }
        try {
            Role role = getRoleById(id);
            roleRepository.deleteById(role.getId());
            log.info("Role deleted with ID: {}", role.getId());
        } catch (Exception e) {
            log.error("Error deleting role with ID {}: {}", id, e.getMessage(), e);
            throw new RoleDeleteException(HttpStatus.CONFLICT, getMessage("role.delete.failed"));
        }
    }

    @Transactional
    public Role createRole(RoleCreateDTO dto) {
        if (dto == null) {
            log.warn("RoleCreateDTO is null");
            throw new RoleCreationException(HttpStatus.BAD_REQUEST, getMessage("role.dto.empty"));
        }
        try {
            Role role = roleMapper.toEntity(dto);
            if (dto.getPrivilegeIds() != null && !dto.getPrivilegeIds().isEmpty()) {
                var privileges = privilegeRepository.findAllById(dto.getPrivilegeIds());
                if (privileges.size() != dto.getPrivilegeIds().size()) {
                    log.warn("Some privileges not found for ids: {}", dto.getPrivilegeIds());
                    throw new RoleCreationException(HttpStatus.BAD_REQUEST, getMessage("role.privileges.not.found"));
                }
                role.setPrivileges(Set.copyOf(privileges));
            }
            Role savedRole = roleRepository.save(role);
            log.info("Role created with ID: {}", savedRole.getId());
            return savedRole;
        } catch (RoleCreationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating role: {}", e.getMessage(), e);
            throw new RoleCreationException(HttpStatus.BAD_REQUEST, getMessage("role.create.failed"));
        }
    }

    @Transactional
    public Role updateRole(Long id, RoleCreateDTO dto) {
        if (id == null) {
            log.warn("Role ID is null for update");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("role.id.empty"));
        }
        if (dto == null) {
            log.warn("RoleCreateDTO is null for update");
            throw new RoleUpdateException(HttpStatus.BAD_REQUEST, getMessage("role.dto.empty"));
        }
        try {
            Role existingRole = getRoleById(id);
            Role roleToUpdate = roleMapper.toEntity(dto);
            roleToUpdate.setId(existingRole.getId());
            if (dto.getPrivilegeIds() != null && !dto.getPrivilegeIds().isEmpty()) {
                var privileges = privilegeRepository.findAllById(dto.getPrivilegeIds());
                if (privileges.size() != dto.getPrivilegeIds().size()) {
                    log.warn("Some privileges not found for ids: {}", dto.getPrivilegeIds());
                    throw new RoleUpdateException(HttpStatus.BAD_REQUEST, getMessage("role.privileges.not.found"));
                }
                roleToUpdate.setPrivileges(Set.copyOf(privileges));
            } else {
                roleToUpdate.setPrivileges(Set.of()); // clear privileges if none provided
            }
            Role updatedRole = roleRepository.save(roleToUpdate);
            log.info("Role updated with ID: {}", updatedRole.getId());
            return updatedRole;
        } catch (Exception e) {
            log.error("Error updating role with ID {}: {}", id, e.getMessage(), e);
            throw new RoleUpdateException(HttpStatus.BAD_REQUEST, getMessage("role.update.failed"));
        }
    }
}
