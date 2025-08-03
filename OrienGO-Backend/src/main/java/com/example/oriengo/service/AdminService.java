package com.example.oriengo.service;

import com.example.oriengo.exception.PathVarException;
import com.example.oriengo.exception.user.UserCreationException;
import com.example.oriengo.exception.user.UserDeleteException;
import com.example.oriengo.exception.user.UserGetException;
import com.example.oriengo.exception.user.UserUpdateException;
import com.example.oriengo.mapper.AdminMapper;
import com.example.oriengo.model.dto.AdminCreateDTO;
import com.example.oriengo.model.entity.Admin;
import com.example.oriengo.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public List<Admin> getAdmins(boolean deleted) {
        try{
            return adminRepository.findByIsDeleted(deleted);
        } catch (Exception e){
            throw new UserGetException(HttpStatus.NOT_FOUND, "No Admin found");
        }
    }

    public Admin getAdminById(Long id, boolean deleted) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Admin ID cannot be empty");
        }
        return adminRepository.findByIdAndIsDeleted(id, deleted)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "Admin not found"));
    }

    public Admin getAdminById(Long id) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Admin ID cannot be empty");
        }
        return adminRepository.findById(id)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "Admin not found"));
    }

    public Admin getAdminByEmail(String email, boolean deleted) {
        if (email == null || email.trim().isEmpty()) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Admin Email cannot be empty");
        }
        return adminRepository.findByEmailAndIsDeleted(email, deleted)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "Admin not found"));
    }

    public void hardDeleteAdmin(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin ID cannot be empty");
        }
        try{
            Admin admin = getAdminById(id);
            adminRepository.deleteById(admin.getId());
            log.info("Admin hard deleted with ID: {}", admin.getId());
        } catch (Exception e) {
            log.error("Error hard deleting admin: {}", e.getMessage());
            throw new UserDeleteException("Failed to hard delete admin");
        }
    }

    public void softDeleteAdmin(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin ID cannot be empty");
        }
        try{
            Admin admin = getAdminById(id, false);
            admin.setDeleted(true);
            admin.setDeletedAt(LocalDateTime.now());
            admin.setEmail("deleted_" + UUID.randomUUID() + "_" + admin.getEmail());
            adminRepository.save(admin);
            log.info("Admin soft deleted with ID: {}", admin.getId());
        } catch (Exception e) {
            log.error("Error soft deleting admin: {}", e.getMessage());
            throw new UserDeleteException("Failed to soft delete admin");
        }
    }

    public Admin createAdmin(AdminCreateDTO dto) {
        try {
            Admin admin = adminMapper.toEntity(dto);
            admin.setPassword(admin.getPassword()); //encoder.encode(admin.getPassword())
            Admin adminOutput = adminRepository.save(admin);
            log.info("Admin created with ID: {}", adminOutput.getId());
            return adminOutput;
        } catch (Exception e) {
            log.error("Error creating admin: {}", e.getMessage());
            throw new UserCreationException("Failed to create admin");
        }
    }

    public Admin updateAdmin(Long id, AdminCreateDTO dto) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Admin ID cannot be empty");
        }
        try {
            Admin admin = adminMapper.toEntity(dto);
            Admin adminChecked = getAdminById(id);
            admin.setId(adminChecked.getId());
            admin.setPassword(admin.getPassword()); //encoder.encode(admin.getPassword())
            Admin adminOutput = adminRepository.save(admin);
            log.info("Admin updated with ID: {}", adminOutput.getId());
            return adminOutput;
        } catch (Exception e) {
            log.error("Error updating admin: {}", e.getMessage());
            throw new UserUpdateException("Failed to update admin");
        }
    }
}
