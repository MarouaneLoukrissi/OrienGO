package com.example.oriengo.service;

import com.example.oriengo.exception.PathVarException;
import com.example.oriengo.exception.user.UserCreationException;
import com.example.oriengo.exception.user.UserDeleteException;
import com.example.oriengo.exception.user.UserGetException;
import com.example.oriengo.exception.user.UserUpdateException;
import com.example.oriengo.mapper.UserMapper;
import com.example.oriengo.model.dto.UserCreateDTO;
import com.example.oriengo.model.entity.User;
import com.example.oriengo.repository.RoleRepository;
import com.example.oriengo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
//    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public List<User> getUsers(boolean deleted) {
        try{
            return userRepository.findByIsDeleted(deleted);
        } catch (Exception e){
            throw new UserGetException(HttpStatus.NOT_FOUND, "No User found");
        }
    }

    public User getUserById(Long id, boolean deleted) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "User ID cannot be empty");
        }
        return userRepository.findByIdAndIsDeleted(id, deleted)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User getUserById(Long id) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "User ID cannot be empty");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User getUserByEmail(String email, boolean deleted) {
        if (email == null || email.trim().isEmpty()) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "User Email cannot be empty");
        }
        return userRepository.findByEmailAndIsDeleted(email, deleted)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<User> getUsersByRole(String role, boolean deleted) {
        if (role == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "User Role cannot be empty");
        }
        try {
            return userRepository.findAllByRoleAndIsDeleted(roleRepository.findByName(role), deleted);
        } catch (Exception e){
            throw new UserGetException(HttpStatus.BAD_REQUEST, "Invalid role: " + role);
        }
    }

    public void hardDeleteUser(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID cannot be empty");
        }
        try{
            User user = getUserById(id);
            userRepository.deleteById(user.getId());
            log.info("User hard deleted with ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Error hard deleting user: {}", e.getMessage());
            throw new UserDeleteException("Failed to hard delete user");
        }
    }

    public void softDeleteUser(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID cannot be empty");
        }
        try{
            User user = getUserById(id, false);
            user.setDeleted(true);
            user.setDeletedAt(LocalDateTime.now());
            user.setEmail("deleted_" + UUID.randomUUID() + "_" + user.getEmail());
            userRepository.save(user);
            log.info("User soft deleted with ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Error soft deleting user: {}", e.getMessage());
            throw new UserDeleteException("Failed to soft delete user");
        }
    }

    public User createUser(UserCreateDTO dto) {
        try {
            User user = userMapper.toEntity(dto);
            user.setPassword(user.getPassword()); //encoder.encode(user.getPassword())
            User userOutput = userRepository.save(user);
            log.info("User created with ID: {}", userOutput.getId());
            return userOutput;
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            throw new UserCreationException("Failed to create user");
        }
    }

    public User updateUser(Long id, UserCreateDTO dto) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "User ID cannot be empty");
        }
        try {
            User user = userMapper.toEntity(dto);
            User userChecked = getUserById(id);
            user.setId(userChecked.getId());
            user.setPassword(user.getPassword()); //encoder.encode(user.getPassword())
            User userOutput = userRepository.save(user);
            log.info("User updated with ID: {}", userOutput.getId());
            return userOutput;
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            throw new UserUpdateException("Failed to update user");
        }
    }
}
