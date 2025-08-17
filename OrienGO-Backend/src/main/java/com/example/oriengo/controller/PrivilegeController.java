package com.example.oriengo.controller;

import com.example.oriengo.mapper.PrivilegeMapper;
import com.example.oriengo.model.dto.PrivilegeCreateDTO;
import com.example.oriengo.model.dto.PrivilegeResponseDTO;
import com.example.oriengo.model.entity.Privilege;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.PrivilegeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("api/privilege")
@RequiredArgsConstructor
@Validated
public class PrivilegeController {

    private final PrivilegeService privilegeService;
    private final PrivilegeMapper privilegeMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PrivilegeResponseDTO>>> getPrivileges() {
        List<Privilege> privileges = privilegeService.getPrivileges();
        List<PrivilegeResponseDTO> privilegeResps = privilegeMapper.toDTO(privileges);
        ApiResponse<List<PrivilegeResponseDTO>> response = ApiResponse.<List<PrivilegeResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Privileges fetched successfully")
                .data(privilegeResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PrivilegeResponseDTO>> getPrivilegeById(@PathVariable Long id) {
        Privilege privilege = privilegeService.getPrivilegeById(id);
        PrivilegeResponseDTO privilegeResp = privilegeMapper.toDTO(privilege);
        ApiResponse<PrivilegeResponseDTO> response = ApiResponse.<PrivilegeResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Privilege fetched successfully")
                .data(privilegeResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePrivilege(@PathVariable Long id) {
        privilegeService.deletePrivilege(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("PRIVILEGE_DELETED")
                .status(204)
                .message("Privilege deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PrivilegeResponseDTO>> createPrivilege(@Valid @RequestBody PrivilegeCreateDTO privilegeInfo) {
        Privilege privilege = privilegeService.createPrivilege(privilegeInfo);
        PrivilegeResponseDTO privilegeResp = privilegeMapper.toDTO(privilege);
        ApiResponse<PrivilegeResponseDTO> response = ApiResponse.<PrivilegeResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Privilege created successfully")
                .data(privilegeResp)
                .build();
        URI location = URI.create("/api/privilege/" + privilege.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PrivilegeResponseDTO>> updatePrivilege(@PathVariable Long id, @Valid @RequestBody PrivilegeCreateDTO privilegeInfo) {
        Privilege privilege = privilegeService.updatePrivilege(id, privilegeInfo);
        PrivilegeResponseDTO privilegeResp = privilegeMapper.toDTO(privilege);
        ApiResponse<PrivilegeResponseDTO> response = ApiResponse.<PrivilegeResponseDTO>builder()
                .code("PRIVILEGE_UPDATED")
                .status(200)
                .message("Privilege updated successfully")
                .data(privilegeResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
