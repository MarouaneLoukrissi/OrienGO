package com.example.oriengo.controller;

import com.example.oriengo.mapper.AdminMapper;
import com.example.oriengo.model.dto.AdminCreateDTO;
import com.example.oriengo.model.dto.AdminResponseDTO;
import com.example.oriengo.model.dto.AdminUpdateDTO;
import com.example.oriengo.model.entity.Admin;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminResponseDTO>>> getAdmins(){
        List<Admin> admins = adminService.getAdmins(false);
        List<AdminResponseDTO> adminResps = adminMapper.toDTO(admins);
        ApiResponse<List<AdminResponseDTO>> response = ApiResponse.<List<AdminResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted")
    public ResponseEntity<ApiResponse<List<AdminResponseDTO>>> getDeletedAdmins(){
        List<Admin> admins = adminService.getAdmins(true);
        List<AdminResponseDTO> adminResps = adminMapper.toDTO(admins);
        ApiResponse<List<AdminResponseDTO>> response = ApiResponse.<List<AdminResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> getAdminById(@PathVariable Long id){
        Admin admin = adminService.getAdminById(id, false);
        AdminResponseDTO adminResp = adminMapper.toDTO(admin);
        ApiResponse<AdminResponseDTO> response = ApiResponse.<AdminResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> getDeletedAdminById(@PathVariable Long id){
        Admin admin = adminService.getAdminById(id, true);
        AdminResponseDTO adminResp = adminMapper.toDTO(admin);
        ApiResponse<AdminResponseDTO> response = ApiResponse.<AdminResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> getAdminByEmail(@PathVariable String email){
        Admin admin = adminService.getAdminByEmail(email, false);
        AdminResponseDTO adminResp = adminMapper.toDTO(admin);
        ApiResponse<AdminResponseDTO> response = ApiResponse.<AdminResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted/email/{email}")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> getDeletedAdminByEmail(@PathVariable String email){
        Admin admin = adminService.getAdminByEmail(email, true);
        AdminResponseDTO adminResp = adminMapper.toDTO(admin);
        ApiResponse<AdminResponseDTO> response = ApiResponse.<AdminResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("hard/{id}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteAdmin(@PathVariable Long id){
        adminService.hardDeleteAdmin(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("ADMIN_DELETED")
                .status(204)
                .message("Admin hard deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteAdmin(@PathVariable Long id){
        adminService.softDeleteAdmin(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("SUCCESS")
                .status(204)
                .message("Admin soft deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> restoreAdmin(@PathVariable Long id){
        Admin admin = adminService.restoreAdmin(id);
        AdminResponseDTO adminResp = adminMapper.toDTO(admin);
        ApiResponse<AdminResponseDTO> response = ApiResponse.<AdminResponseDTO>builder()
                .code("ADMIN_RESTORED")
                .status(200)
                .message("Admin restored successfully")
                .data(adminResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdminResponseDTO>> createAdmin(@Valid @RequestBody AdminCreateDTO adminInfo){
        Admin admin = adminService.createAdmin(adminInfo);
        AdminResponseDTO adminResp = adminMapper.toDTO(admin);
        ApiResponse<AdminResponseDTO> response = ApiResponse.<AdminResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Admin created successfully")
                .data(adminResp)
                .build();
        URI location = URI.create("/api/admin/" + admin.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> updateAdmin(@PathVariable Long id, @Valid @RequestBody AdminUpdateDTO adminInfo){
        Admin admin = adminService.updateAdmin(id, adminInfo);
        AdminResponseDTO adminResp = adminMapper.toDTO(admin);
        ApiResponse<AdminResponseDTO> response = ApiResponse.<AdminResponseDTO>builder()
                .code("ADMIN_UPDATED")
                .status(200)
                .message("Admin updated successfully")
                .data(adminResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
