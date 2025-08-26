package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.user.*;
import com.example.oriengo.mapper.AdminMapper;
import com.example.oriengo.model.dto.AdminCreateDTO;
import com.example.oriengo.model.dto.AdminDTO;
import com.example.oriengo.model.dto.AdminModifyDTO;
import com.example.oriengo.model.dto.AdminUpdateDTO;
import com.example.oriengo.model.entity.Admin;
import com.example.oriengo.model.entity.Role;
import com.example.oriengo.repository.AdminRepository;
import com.example.oriengo.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final RoleRepository roleRepository;
    //    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final MessageSource messageSource; // Injected

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public List<Admin> getAdmins(boolean deleted) {
        try {
            log.info("Fetching admins with isDeleted = {}", deleted);
            List<Admin> admins = adminRepository.findByIsDeleted(deleted);
            log.info("Found {} admins", admins.size());
            return admins;
        } catch (Exception e) {
            log.error("Failed to fetch admins with isDeleted = {}: {}", deleted, e.getMessage(), e);
            throw new UserGetException(HttpStatus.NOT_FOUND, getMessage("admin.not.found"));
        }
    }

    public List<Admin> getAdmins() {
        try {
            log.info("Fetching admins");
            List<Admin> admins = adminRepository.findAll();
            log.info("Found {} admins", admins.size());
            return admins;
        } catch (Exception e) {
            log.error("Failed to fetch admins: {}", e.getMessage(), e);
            throw new UserGetException(HttpStatus.NOT_FOUND, getMessage("admin.not.found"));
        }
    }

    public Admin getAdminById(Long id, boolean deleted) {
        if (id == null) {
            log.warn("Attempted to fetch admin with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("admin.id.empty"));
        }

        log.info("Fetching admin with ID: {} and isDeleted = {}", id, deleted);

        return adminRepository.findByIdAndIsDeleted(id, deleted)
                .orElseThrow(() -> {
                    log.error("Admin not found with ID: {} and isDeleted = {}", id, deleted);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("admin.not.found"));
                });
    }

    public Admin getAdminById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch admin with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("admin.id.empty"));
        }

        log.info("Fetching admin with ID: {}", id);

        return adminRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> {
                    log.error("Admin not found with ID: {}", id);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("admin.not.found"));
                });
    }

    public Admin getAdminByEmail(String email, boolean deleted) {
        if (email == null || email.trim().isEmpty()) {
            log.warn("Attempted to fetch admin with null or empty email");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("admin.email.empty"));
        }

        log.info("Fetching admin with email: {} (deleted = {})", email, deleted);

        return adminRepository.findByEmailAndIsDeleted(email, deleted)
                .orElseThrow(() -> {
                    log.error("Admin not found with email: {}", email);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("admin.not.found"));
                });
    }

    @Transactional
    public void hardDeleteAdmin(Long id) {
        if (id == null) {
            log.warn("Attempted hard delete with null admin ID");
            throw new UserDeleteException(HttpStatus.BAD_REQUEST, getMessage("admin.id.empty"));
        }

        try {
            log.info("Attempting hard delete for admin with ID: {}", id);

            Admin admin = getAdminById(id);
            adminRepository.deleteById(admin.getId());

            log.info("Successfully hard deleted admin with ID: {}", admin.getId());
        } catch (Exception e) {
            log.error("Error during hard delete of admin with ID {}: {}", id, e.getMessage(), e);
            throw new UserDeleteException(HttpStatus.CONFLICT, getMessage("admin.hard.delete.failed"));
        }
    }

    @Transactional
    public void softDeleteAdmin(Long id) {
        if (id == null) {
            log.warn("Attempted soft delete with null admin ID");
            throw new UserDeleteException(HttpStatus.BAD_REQUEST, getMessage("admin.id.empty"));
        }

        try {
            log.info("Attempting soft delete for admin with ID: {}", id);

            Admin admin = getAdminById(id, false);
            String originalEmail = admin.getEmail();

            admin.setDeleted(true);
            admin.setDeletedAt(LocalDateTime.now());
            admin.setEmail("deleted_" + UUID.randomUUID() + "_" + originalEmail);

            adminRepository.save(admin);

            log.info("Successfully soft deleted admin with ID: {}", admin.getId());
        } catch (Exception e) {
            log.error("Error during soft delete of admin with ID {}: {}", id, e.getMessage(), e);
            throw new UserDeleteException(HttpStatus.CONFLICT, getMessage("admin.soft.delete.failed"));
        }
    }

    @Transactional
    public Admin restoreAdmin(Long id) {
        if (id == null) {
            log.warn("Attempted restore with null admin ID");
            throw new UserRestoreException(HttpStatus.BAD_REQUEST, getMessage("admin.id.empty"));
        }

        try {
            log.info("Attempting restore for admin with ID: {}", id);

            Admin admin = getAdminById(id, true);
            String deletedEmail = admin.getEmail();

            // Extract original email from deleted email format
            String prefix = "deleted_";
            int index = deletedEmail.indexOf("_", prefix.length()); // find the 2nd underscore
            if (index == -1 || index + 1 >= deletedEmail.length()) {
                throw new UserRestoreException(HttpStatus.CONFLICT, getMessage("admin.restore.email.invalid"));
            }

            String originalEmail = deletedEmail.substring(index + 1);

            // Check if email is already in use
            boolean emailTaken = adminRepository.existsByEmailAndIsDeletedFalse(originalEmail);
            if (emailTaken) {
                log.warn("Original email {} is already in use. Cannot restore to original email.", originalEmail);
                throw new UserRestoreException(HttpStatus.CONFLICT, getMessage("admin.restore.email.used"));
            }

            admin.setDeleted(false);
            admin.setDeletedAt(null);
            admin.setEmail(originalEmail); // restore original email

            adminRepository.save(admin);

            log.info("Successfully restore admin with ID: {}", admin.getId());
            return admin;
        } catch (Exception e) {
            log.error("Error during restore of admin with ID {}: {}", id, e.getMessage(), e);
            throw new UserRestoreException(HttpStatus.BAD_REQUEST, getMessage("admin.restore.failed"));
        }
    }

    @Transactional
    public Admin createAdmin(AdminCreateDTO dto) {
        if (dto == null) {
            log.warn("admin request cannot be null");
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("admin.dto.empty"));
        }
        try {
            log.info("Starting creation of new admin with email: {}", dto.getEmail());
            // Check if email already exists
            try {
                Admin existingAdmin = getAdminByEmail(dto.getEmail(), false);
                if (existingAdmin != null) {
                    log.warn("Admin already exists with email: {}", dto.getEmail());
                    throw new UserCreationException(HttpStatus.CONFLICT, getMessage("admin.email.already.exists", dto.getEmail()));
                }
            } catch (UserGetException e) {
                // No admin found: continue (expected case)
                log.debug("No existing admin found with email {}, proceeding with creation", dto.getEmail());
            }

            Admin admin = adminMapper.toEntity(dto);
            // Encode password before saving if applicable
            admin.setPassword(admin.getPassword()); // e.g., passwordEncoder.encode(dto.getPassword());
            admin.setEnabled(true);

            String roleName = switch (admin.getAdminLevel()) {
                case MANAGER -> "MANAGER";
                case STANDARD_ADMIN -> "STANDARD_ADMIN";
                default -> {
                    log.warn("Invalid admin level provided: {}", admin.getAdminLevel());
                    throw new UserCreationException(HttpStatus.NOT_FOUND, getMessage("admin.level.invalid"));
                }
            };

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> {
                        log.warn("Role '{}' not found in database", roleName);
                        return new UserCreationException(HttpStatus.NOT_FOUND , getMessage("admin.role.not.found", roleName));
                    });

            admin.setRoles(Set.of(role));

            Admin savedAdmin = adminRepository.save(admin);
            log.info("Admin created successfully with ID: {} and role: {}", savedAdmin.getId(), roleName);

            return savedAdmin;

        } catch (UserCreationException e) {
            // Already logged; just rethrow
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during admin creation for email {}: {}", dto.getEmail(), e.getMessage(), e);
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("admin.create.failed"));
        }
    }

    @Transactional
    public Admin createAdminFromDTO(AdminDTO dto) {
        if (dto == null) {
            log.warn("Admin DTO cannot be null");
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("admin.dto.empty"));
        }

        try {
            log.info("Starting creation of new admin with email: {}", dto.getEmail());

            // Check if email already exists
            try {
                Admin existingAdmin = getAdminByEmail(dto.getEmail(), false);
                if (existingAdmin != null) {
                    log.warn("Admin already exists with email: {}", dto.getEmail());
                    throw new UserCreationException(HttpStatus.CONFLICT,
                            getMessage("admin.email.already.exists", dto.getEmail()));
                }
            } catch (UserGetException e) {
                log.debug("No existing admin found with email {}, proceeding with creation", dto.getEmail());
            }

            // Map DTO to entity
            Admin admin = adminMapper.toEntity(dto);

            // Encode password (replace with passwordEncoder.encode if needed)
            admin.setPassword(admin.getPassword());
            admin.setEnabled(true);

            // Determine role from admin level
            String roleName = switch (admin.getAdminLevel()) {
                case MANAGER -> "MANAGER";
                case STANDARD_ADMIN -> "STANDARD_ADMIN";
                default -> {
                    log.warn("Invalid admin level provided: {}", admin.getAdminLevel());
                    throw new UserCreationException(HttpStatus.NOT_FOUND, getMessage("admin.level.invalid"));
                }
            };

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> {
                        log.warn("Role '{}' not found in database", roleName);
                        return new UserCreationException(HttpStatus.NOT_FOUND, getMessage("admin.role.not.found", roleName));
                    });

            admin.setRoles(Set.of(role));

            // Set createdBy if applicable
            if (dto.getCreatedById() != null) {
                Admin creator = adminRepository.findByIdIncludingDeleted(dto.getCreatedById())
                        .orElseThrow(() -> new UserCreationException(HttpStatus.NOT_FOUND,
                                getMessage("admin.creator.not.found", dto.getCreatedById().toString())));
                admin.setCreatedBy(creator);
            }

            // Save admin
            Admin savedAdmin = adminRepository.save(admin);
            log.info("Admin created successfully with ID: {} and role: {}", savedAdmin.getId(), roleName);

            return savedAdmin;

        } catch (UserCreationException e) {
            throw e; // already logged
        } catch (Exception e) {
            log.error("Unexpected error during admin creation for email {}: {}", dto.getEmail(), e.getMessage(), e);
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("admin.create.failed"));
        }
    }

    @Transactional
    public Admin updateAdmin(Long id, AdminUpdateDTO dto) {
        if (id == null) {
            log.warn("Attempted to update admin with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("admin.id.empty"));
        } else if (dto == null) {
            log.warn("admin request cannot be null");
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("admin.dto.empty"));
        }
        try {
            Admin admin = adminMapper.toEntity(dto);
            log.info("Updating admin with ID: {}", id);
            Admin existingAdmin = getAdminById(id);

//            // Update fields explicitly, example:
//            adminChecked.setFirstName(dto.getFirstName());
//            adminChecked.setLastName(dto.getLastName());
//            adminChecked.setAge(dto.getAge());
//            adminChecked.setPhoneNumber(dto.getPhoneNumber());
//            adminChecked.setGender(dto.getGender());
//            adminChecked.setAdminLevel(dto.getAdminLevel());
//            adminChecked.setDepartment(dto.getDepartment());
            adminMapper.updateAdminFromDto(dto, existingAdmin);


            // Update password if provided (and encode if needed)
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                existingAdmin.setPassword(dto.getPassword());
            }

            // Update roles if applicable (as explained before)
            String roleName = switch (dto.getAdminLevel()) {
                case MANAGER -> "MANAGER";
                case STANDARD_ADMIN -> "STANDARD_ADMIN";
            };

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new UserUpdateException(HttpStatus.NOT_FOUND, getMessage("admin.role.not.found", roleName)));
            existingAdmin.getRoles().clear();
            existingAdmin.getRoles().add(role);

            Admin savedAdmin = adminRepository.save(existingAdmin);
            log.info("Admin with ID {} successfully updated", savedAdmin.getId());
            return savedAdmin;
        } catch (Exception e) {
            log.error("Error updating admin with ID {}: {}", id, e.getMessage(), e);
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("admin.update.failed"));
        }
    }

    @Transactional
    public Admin updateAdminWithModifyDTO(Long id, AdminModifyDTO dto) {
        if (id == null) {
            log.warn("Attempted to update admin with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("admin.id.empty"));
        } else if (dto == null) {
            log.warn("Admin modify request cannot be null");
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("admin.dto.empty"));
        }

        try {
            log.info("Updating admin with ID: {}", id);
            Admin existingAdmin = getAdminById(id);

            // Update fields from DTO
            adminMapper.updateAdminFromDto(dto, existingAdmin);

            // Update password if provided
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                existingAdmin.setPassword(dto.getPassword()); // e.g., passwordEncoder.encode(dto.getPassword())
            }

            // Update role based on adminLevel
            String roleName = switch (existingAdmin.getAdminLevel()) {
                case MANAGER -> "MANAGER";
                case STANDARD_ADMIN -> "STANDARD_ADMIN";
                default -> {
                    log.warn("Invalid admin level provided: {}", existingAdmin.getAdminLevel());
                    throw new UserUpdateException(HttpStatus.NOT_FOUND, getMessage("admin.level.invalid"));
                }
            };

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> {
                        log.warn("Role '{}' not found in database", roleName);
                        return new UserUpdateException(HttpStatus.NOT_FOUND, getMessage("admin.role.not.found", roleName));
                    });

            // Use mutable set
            existingAdmin.setRoles(new HashSet<>(Set.of(role)));

            // Update createdBy if necessary
//            if (dto.getCreatedById() != null) {
//                Admin creator = adminRepository.findById(dto.getCreatedById())
//                        .orElseThrow(() -> new UserUpdateException(HttpStatus.NOT_FOUND,
//                                getMessage("admin.creator.not.found", dto.getCreatedById().toString())));
//                existingAdmin.setCreatedBy(creator);
//            }

            Admin savedAdmin = adminRepository.save(existingAdmin);
            log.info("Admin with ID {} successfully updated with role: {}", savedAdmin.getId(), roleName);
            return savedAdmin;

        } catch (UserUpdateException e) {
            // Already logged; just rethrow
            throw e;
        } catch (Exception e) {
            log.error("Error updating admin with ID {}: {}", id, e.getMessage(), e);
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("admin.update.failed"));
        }
    }
}
