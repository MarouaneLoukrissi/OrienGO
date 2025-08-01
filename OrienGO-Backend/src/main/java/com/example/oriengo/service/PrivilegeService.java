package com.example.oriengo.service;

import com.example.oriengo.exception.PathVarException;
import com.example.oriengo.exception.Privilege.PrivilegeCreationException;
import com.example.oriengo.exception.Privilege.PrivilegeDeleteException;
import com.example.oriengo.exception.Privilege.PrivilegeGetException;
import com.example.oriengo.exception.Privilege.PrivilegeUpdateException;
import com.example.oriengo.mapper.PrivilegeMapper;
import com.example.oriengo.model.dto.PrivilegeCreateDTO;
import com.example.oriengo.model.entity.Privilege;

import com.example.oriengo.repository.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeMapper privilegeMapper;

    public List<Privilege> getPrivileges() {
        try{
            return privilegeRepository.findAll();
        } catch (Exception e){
            throw new PrivilegeGetException(HttpStatus.NOT_FOUND, "No Privilege found");
        }
    }

    public Privilege getPrivilegeById(Long id) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Privilege ID cannot be empty");
        }
        return privilegeRepository.findById(id)
                .orElseThrow(() -> new PrivilegeGetException(HttpStatus.NOT_FOUND, "Privilege not found"));
    }

    public Privilege getPrivilegeByName(String name) {
        if (name == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Privilege name cannot be empty");
        }
        return privilegeRepository.findByName(name)
                .orElseThrow(() -> new PrivilegeGetException(HttpStatus.NOT_FOUND, "Privilege not found"));
    }

    public void deletePrivilege(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Privilege ID cannot be empty");
        }
        try{
            Privilege privilege = getPrivilegeById(id);
            privilegeRepository.deleteById(privilege.getId());
            log.info("Privilege deleted with ID: {}", privilege.getId());
        } catch (Exception e) {
            log.error("Error deleting privilege: {}", e.getMessage());
            throw new PrivilegeDeleteException("Failed to delete privilege");
        }
    }

    public Privilege createPrivilege(PrivilegeCreateDTO dto) {
        try {
            Privilege privilege = privilegeMapper.toEntity(dto);
            Privilege privilegeOutput = privilegeRepository.save(privilege);
            log.info("Privilege created with ID: {}", privilegeOutput.getId());
            return privilegeOutput;
        } catch (Exception e) {
            log.error("Error creating privilege: {}", e.getMessage());
            throw new PrivilegeCreationException("Failed to create privilege");
        }
    }

    public Privilege updatePrivilege(Long id, PrivilegeCreateDTO dto) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Privilege ID cannot be empty");
        }
        try {
            Privilege privilege = privilegeMapper.toEntity(dto);
            Privilege privilegeChecked = getPrivilegeById(id);
            privilege.setId(privilegeChecked.getId());
            Privilege privilegeOutput = privilegeRepository.save(privilege);
            log.info("Privilege updated with ID: {}", privilegeOutput.getId());
            return privilegeOutput;
        } catch (Exception e) {
            log.error("Error updating privilege: {}", e.getMessage());
            throw new PrivilegeUpdateException("Failed to update privilege");
        }
    }
}
