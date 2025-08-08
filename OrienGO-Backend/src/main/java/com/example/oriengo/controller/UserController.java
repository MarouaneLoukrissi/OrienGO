package com.example.oriengo.controller;

import com.example.oriengo.model.dto.UserCreateDTO;
import com.example.oriengo.model.dto.UserResponseDTO;
import com.example.oriengo.model.entity.User;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.UserService;
import com.example.oriengo.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsers() {
        List<User> users = userService.getUsers(false);
        List<UserResponseDTO> userResponses = userMapper.toDTO(users);
        ApiResponse<List<UserResponseDTO>> response = ApiResponse.<List<UserResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Users fetched successfully")
                .data(userResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getDeletedUsers() {
        List<User> users = userService.getUsers(true);
        List<UserResponseDTO> userResponses = userMapper.toDTO(users);
        ApiResponse<List<UserResponseDTO>> response = ApiResponse.<List<UserResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted users fetched successfully")
                .data(userResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id, false);
        UserResponseDTO userResp = userMapper.toDTO(user);
        ApiResponse<UserResponseDTO> response = ApiResponse.<UserResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("User fetched successfully")
                .data(userResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getDeletedUserById(@PathVariable Long id) {
        User user = userService.getUserById(id, true);
        UserResponseDTO userResp = userMapper.toDTO(user);
        ApiResponse<UserResponseDTO> response = ApiResponse.<UserResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted user fetched successfully")
                .data(userResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email, false);
        UserResponseDTO userResp = userMapper.toDTO(user);
        ApiResponse<UserResponseDTO> response = ApiResponse.<UserResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("User fetched successfully by email")
                .data(userResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted/email/{email}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getDeletedUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email, true);
        UserResponseDTO userResp = userMapper.toDTO(user);
        ApiResponse<UserResponseDTO> response = ApiResponse.<UserResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted user fetched successfully by email")
                .data(userResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsersByRole(@PathVariable String role) {
        List<User> users = userService.getUsersByRole(role, false);
        List<UserResponseDTO> userResponses = userMapper.toDTO(users);
        ApiResponse<List<UserResponseDTO>> response = ApiResponse.<List<UserResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Users fetched successfully by role")
                .data(userResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted/role/{role}")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getDeletedUsersByRole(@PathVariable String role) {
        List<User> users = userService.getUsersByRole(role, true);
        List<UserResponseDTO> userResponses = userMapper.toDTO(users);
        ApiResponse<List<UserResponseDTO>> response = ApiResponse.<List<UserResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted users fetched successfully by role")
                .data(userResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteUser(@PathVariable Long id) {
        userService.hardDeleteUser(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("USER_HARD_DELETED")
                .status(204)
                .message("User hard deleted successfully")
                .build();
        return ResponseEntity.status(204).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("USER_SOFT_DELETED")
                .status(204)
                .message("User soft deleted successfully")
                .build();
        return ResponseEntity.status(204).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody UserCreateDTO userInfo) {
        User user = userService.createUser(userInfo);
        UserResponseDTO userResp = userMapper.toDTO(user);
        ApiResponse<UserResponseDTO> response = ApiResponse.<UserResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("User created successfully")
                .data(userResp)
                .build();
        URI location = URI.create("/api/user/" + user.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateDTO userInfo) {
        User user = userService.updateUser(id, userInfo);
        UserResponseDTO userResp = userMapper.toDTO(user);
        ApiResponse<UserResponseDTO> response = ApiResponse.<UserResponseDTO>builder()
                .code("USER_UPDATED")
                .status(200)
                .message("User updated successfully")
                .data(userResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
