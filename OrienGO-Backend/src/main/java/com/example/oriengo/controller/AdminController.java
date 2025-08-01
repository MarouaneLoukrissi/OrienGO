package com.example.oriengo.controller;

import com.example.oriengo.model.dto.AdminCreateDTO;
import com.example.oriengo.model.entity.Admin;
import com.example.oriengo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<Admin>> getAdmins(){
        List<Admin> admins = adminService.getAdmins(false);
        return ResponseEntity.ok(admins);
    }

    @GetMapping("deleted")
    public ResponseEntity<List<Admin>> getDeletedAdmins(){
        List<Admin> admins = adminService.getAdmins(true);
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id){
        Admin admin = adminService.getAdminById(id, false);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<Admin> getDeletedAdminById(@PathVariable Long id){
        Admin admin = adminService.getAdminById(id, true);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Admin> getAdminByEmail(@PathVariable String email){
        Admin admin = adminService.getAdminByEmail(email, false);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("deleted/email/{email}")
    public ResponseEntity<Admin> getDeletedAdminByEmail(@PathVariable String email){
        Admin admin = adminService.getAdminByEmail(email, true);
        return ResponseEntity.ok(admin);
    }

    @DeleteMapping("hard/{id}")
    public ResponseEntity<Void> hardDeleteAdmin(@PathVariable Long id){
        adminService.hardDeleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteAdmin(@PathVariable Long id){
        adminService.softDeleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Admin> createAdmin(@Validated @RequestBody AdminCreateDTO adminInfo){
        Admin admin = adminService.createAdmin(adminInfo);
        URI location = URI.create("/api/admin/" + admin.getId());
        return ResponseEntity.created(location).body(admin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @Validated @RequestBody AdminCreateDTO adminInfo){
        Admin admin = adminService.updateAdmin(id, adminInfo);
        return ResponseEntity.ok(admin);
    }
}
