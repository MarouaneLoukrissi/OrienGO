package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.user.UserCreationException;
import com.example.oriengo.exception.custom.user.UserDeleteException;
import com.example.oriengo.exception.custom.user.UserGetException;
import com.example.oriengo.exception.custom.user.UserUpdateException;
import com.example.oriengo.mapper.UserMapper;
import com.example.oriengo.model.dto.UserCreateDTO;
import com.example.oriengo.model.entity.User;
import com.example.oriengo.repository.RoleRepository;
import com.example.oriengo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final MessageSource messageSource;

    private String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
    }

    public List<User> getUsers(boolean deleted) {
        try {
            log.info("Fetching all users with deleted={}", deleted);
            List<User> users = userRepository.findByIsDeleted(deleted);
            log.info("Found {} users", users.size());
            return users;
        } catch (Exception e) {
            log.error("Failed to fetch users: {}", e.getMessage(), e);
            throw new UserGetException(HttpStatus.NOT_FOUND, getMessage("user.not.found"));
        }
    }

    public long countUsersByRoles(boolean deleted, List<String> roles) {
        try {
            log.info("Counting users with deleted={} and roles in {}", deleted, roles);
            long count = userRepository.countUsersByDeletedAndRoles(deleted, roles);
            log.info("Found {} users", count);
            return count;
        } catch (Exception e) {
            log.error("Failed to count users: {}", e.getMessage(), e);
            throw new UserGetException(HttpStatus.NOT_FOUND, getMessage("user.not.found"));
        }
    }

    public User getUserById(Long id, boolean deleted) {
        if (id == null) {
            log.warn("User ID is null");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("user.id.empty"));
        }
        return userRepository.findByIdAndIsDeleted(id, deleted)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("user.not.found"));
                });
    }

    public User getUserById(Long id) {
        if (id == null) {
            log.warn("User ID is null");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("user.id.empty"));
        }
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("user.not.found"));
                });
    }

    public User getUserByEmail(String email, boolean deleted) {
        if (email == null || email.trim().isEmpty()) {
            log.warn("User email is null or empty");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("user.email.empty"));
        }
        return userRepository.findByEmailAndIsDeleted(email, deleted)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("user.not.found"));
                });
    }

    public List<User> getUsersByRole(String role, boolean deleted) {
        if (role == null) {
            log.warn("User role is null");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("user.role.empty"));
        }
        try {
            var roleEntity = roleRepository.findByName(role)
                    .orElseThrow(() -> new UserGetException(HttpStatus.BAD_REQUEST, getMessage("user.role.invalid", role)));
            List<User> users = userRepository.findAllByRoleAndIsDeleted(roleEntity, deleted);
            log.info("Found {} users with role '{}'", users.size(), role);
            return users;
        } catch (UserGetException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching users by role {}: {}", role, e.getMessage());
            throw new UserGetException(HttpStatus.BAD_REQUEST, getMessage("user.role.invalid", role));
        }
    }

    @Transactional
    public void hardDeleteUser(Long id) {
        if (id == null) {
            log.warn("User ID is null for hard deletion");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("user.id.empty"));
        }
        try {
            User user = getUserById(id);
            userRepository.deleteById(user.getId());
            log.info("User hard deleted with ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Error hard deleting user with ID {}: {}", id, e.getMessage(), e);
            throw new UserDeleteException(HttpStatus.BAD_REQUEST, getMessage("user.delete.failed"));
        }
    }

    @Transactional
    public void softDeleteUser(Long id) {
        if (id == null) {
            log.warn("User ID is null for soft deletion");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("user.id.empty"));
        }
        try {
            User user = getUserById(id, false);
            user.setDeleted(true);
            user.setDeletedAt(LocalDateTime.now());
            user.setEmail("deleted_" + UUID.randomUUID() + "_" + user.getEmail());
            userRepository.save(user);
            log.info("User soft deleted with ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Error soft deleting user with ID {}: {}", id, e.getMessage(), e);
            throw new UserDeleteException(HttpStatus.BAD_REQUEST, getMessage("user.delete.failed"));
        }
    }

    @Transactional
    public User createUser(UserCreateDTO dto) {
        if (dto == null) {
            log.warn("UserCreateDTO is null");
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("user.dto.empty"));
        }
        try {
            User user = userMapper.toEntity(dto);
            // user.setPassword(encoder.encode(user.getPassword())); // if you want to encode later
            User savedUser = userRepository.save(user);
            log.info("User created with ID: {}", savedUser.getId());
            return savedUser;
        } catch (UserCreationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("user.create.failed"));
        }
    }

    @Transactional
    public User updateUser(Long id, UserCreateDTO dto) {
        if (id == null) {
            log.warn("User ID is null for update");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("user.id.empty"));
        }
        if (dto == null) {
            log.warn("UserCreateDTO is null for update");
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("user.dto.empty"));
        }
        try {
            User existingUser = getUserById(id);
            User userToUpdate = userMapper.toEntity(dto);
            userToUpdate.setId(existingUser.getId());
            // userToUpdate.setPassword(encoder.encode(userToUpdate.getPassword())); // encode if needed
            User updatedUser = userRepository.save(userToUpdate);
            log.info("User updated with ID: {}", updatedUser.getId());
            return updatedUser;
        } catch (Exception e) {
            log.error("Error updating user with ID {}: {}", id, e.getMessage(), e);
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("user.update.failed"));
        }
    }
}
