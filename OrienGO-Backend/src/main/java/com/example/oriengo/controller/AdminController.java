package com.example.oriengo.controller;

import com.example.oriengo.mapper.AdminMapper;
import com.example.oriengo.model.dto.*;
import com.example.oriengo.model.entity.Admin;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminReturnDTO>>> getAdmins(){
        List<Admin> admins = adminService.getAdmins();
        List<AdminReturnDTO> adminResps = adminMapper.toReturnDTO(admins);
        ApiResponse<List<AdminReturnDTO>> response = ApiResponse.<List<AdminReturnDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("active")
    public ResponseEntity<ApiResponse<List<AdminReturnDTO>>> getActiveAdmins(){
        List<Admin> admins = adminService.getAdmins(false);
        List<AdminReturnDTO> adminResps = adminMapper.toReturnDTO(admins);
        ApiResponse<List<AdminReturnDTO>> response = ApiResponse.<List<AdminReturnDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted")
    public ResponseEntity<ApiResponse<List<AdminReturnDTO>>> getDeletedAdmins(){
        List<Admin> admins = adminService.getAdmins(true);
        List<AdminReturnDTO> adminResps = adminMapper.toReturnDTO(admins);
        ApiResponse<List<AdminReturnDTO>> response = ApiResponse.<List<AdminReturnDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminReturnDTO>> getAdminById(@PathVariable Long id){
        Admin admin = adminService.getAdminById(id, false);
        AdminReturnDTO adminResp = adminMapper.toReturnDTO(admin);
        ApiResponse<AdminReturnDTO> response = ApiResponse.<AdminReturnDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<ApiResponse<AdminReturnDTO>> getDeletedAdminById(@PathVariable Long id){
        Admin admin = adminService.getAdminById(id, true);
        AdminReturnDTO adminResp = adminMapper.toReturnDTO(admin);
        ApiResponse<AdminReturnDTO> response = ApiResponse.<AdminReturnDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<AdminReturnDTO>> getAdminByEmail(@PathVariable String email){
        Admin admin = adminService.getAdminByEmail(email, false);
        AdminReturnDTO adminResp = adminMapper.toReturnDTO(admin);
        ApiResponse<AdminReturnDTO> response = ApiResponse.<AdminReturnDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Admins fetched successfully")
                .data(adminResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted/email/{email}")
    public ResponseEntity<ApiResponse<AdminReturnDTO>> getDeletedAdminByEmail(@PathVariable String email){
        Admin admin = adminService.getAdminByEmail(email, true);
        AdminReturnDTO adminResp = adminMapper.toReturnDTO(admin);
        ApiResponse<AdminReturnDTO> response = ApiResponse.<AdminReturnDTO>builder()
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
    public ResponseEntity<ApiResponse<AdminReturnDTO>> restoreAdmin(@PathVariable Long id){
        Admin admin = adminService.restoreAdmin(id);
        AdminReturnDTO adminResp = adminMapper.toReturnDTO(admin);
        ApiResponse<AdminReturnDTO> response = ApiResponse.<AdminReturnDTO>builder()
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

    @PostMapping("all-fields")
    public ResponseEntity<ApiResponse<AdminReturnDTO>> createAdmin(@Valid @RequestBody AdminDTO adminDTO) {
        // Call the service method
        Admin createdAdmin = adminService.createAdminFromDTO(adminDTO);

        // Map entity to response DTO
        AdminReturnDTO adminResp = adminMapper.toReturnDTO(createdAdmin);

        // Build API response
        ApiResponse<AdminReturnDTO> response = ApiResponse.<AdminReturnDTO>builder()
                .code("SUCCESS")
                .status(HttpStatus.CREATED.value())
                .message("Admin created successfully")
                .data(adminResp)
                .build();

        // Set Location header
        URI location = URI.create("/api/admin/" + createdAdmin.getId());

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

    @PutMapping("/{id}/all-fields")
    public ResponseEntity<ApiResponse<AdminReturnDTO>> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminModifyDTO adminDTO) {

        // Call the service method to update the admin
        Admin updatedAdmin = adminService.updateAdminWithModifyDTO(id, adminDTO);

        // Map entity to response DTO
        AdminReturnDTO adminResp = adminMapper.toReturnDTO(updatedAdmin);

        // Build API response
        ApiResponse<AdminReturnDTO> response = ApiResponse.<AdminReturnDTO>builder()
                .code("ADMIN_UPDATED")
                .status(HttpStatus.OK.value())
                .message("Admin updated successfully")
                .data(adminResp)
                .build();

        return ResponseEntity.ok(response);
    }
}
