package com.example.oriengo.service;

import com.example.oriengo.exception.Role.RoleUpdateException;
import com.example.oriengo.exception.Role.RoleCreationException;
import com.example.oriengo.exception.Role.RoleDeleteException;
import com.example.oriengo.exception.Role.RoleGetException;
import com.example.oriengo.exception.PathVarException;
import com.example.oriengo.mapper.RoleMapper;
import com.example.oriengo.model.dto.RoleCreateDTO;
import com.example.oriengo.model.entity.Role;
import com.example.oriengo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<Role> getRoles() {
        try{
            return roleRepository.findAll();
        } catch (Exception e){
            throw new RoleGetException(HttpStatus.NOT_FOUND, "No Role found");
        }
    }

    public Role getRoleById(Long id) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Role ID cannot be empty");
        }
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleGetException(HttpStatus.NOT_FOUND, "Role not found"));
    }
    
    public void deleteRole(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role ID cannot be empty");
        }
        try{
            Role role = getRoleById(id);
            roleRepository.deleteById(role.getId());
            log.info("Role deleted with ID: {}", role.getId());
        } catch (Exception e) {
            log.error("Error deleting role: {}", e.getMessage());
            throw new RoleDeleteException("Failed to delete role");
        }
    }

    public Role createRole(RoleCreateDTO dto) {
        try {
            Role role = roleMapper.toEntity(dto);
            Role roleOutput = roleRepository.save(role);
            log.info("Role created with ID: {}", roleOutput.getId());
            return roleOutput;
        } catch (Exception e) {
            log.error("Error creating role: {}", e.getMessage());
            throw new RoleCreationException("Failed to create role");
        }
    }

    public Role updateRole(Long id, RoleCreateDTO dto) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Role ID cannot be empty");
        }
        try {
            Role role = roleMapper.toEntity(dto);
            Role roleChecked = getRoleById(id);
            role.setId(roleChecked.getId());
            Role roleOutput = roleRepository.save(role);
            log.info("Role updated with ID: {}", roleOutput.getId());
            return roleOutput;
        } catch (Exception e) {
            log.error("Error updating role: {}", e.getMessage());
            throw new RoleUpdateException("Failed to update role");
        }
    }
}
