package com.example.oriengo.controller;

import com.example.oriengo.mapper.RoleMapper;
import com.example.oriengo.model.dto.RoleCreateDTO;
import com.example.oriengo.model.dto.RoleResponseDTO;
import com.example.oriengo.model.entity.Role;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("api/role")
@RequiredArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponseDTO>>> getRoles() {
        List<Role> roles = roleService.getRoles();
        List<RoleResponseDTO> roleResps = roleMapper.toDTO(roles);
        ApiResponse<List<RoleResponseDTO>> response = ApiResponse.<List<RoleResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Roles fetched successfully")
                .data(roleResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        RoleResponseDTO roleResp = roleMapper.toDTO(role);
        ApiResponse<RoleResponseDTO> response = ApiResponse.<RoleResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Role fetched successfully")
                .data(roleResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("ROLE_DELETED")
                .status(204)
                .message("Role deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponseDTO>> createRole(@Valid @RequestBody RoleCreateDTO roleInfo) {
        Role role = roleService.createRole(roleInfo);
        RoleResponseDTO roleResp = roleMapper.toDTO(role);
        ApiResponse<RoleResponseDTO> response = ApiResponse.<RoleResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Role created successfully")
                .data(roleResp)
                .build();
        URI location = URI.create("/api/role/" + role.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> updateRole(@PathVariable Long id, @Valid @RequestBody RoleCreateDTO roleInfo) {
        Role role = roleService.updateRole(id, roleInfo);
        RoleResponseDTO roleResp = roleMapper.toDTO(role);
        ApiResponse<RoleResponseDTO> response = ApiResponse.<RoleResponseDTO>builder()
                .code("ROLE_UPDATED")
                .status(200)
                .message("Role updated successfully")
                .data(roleResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
