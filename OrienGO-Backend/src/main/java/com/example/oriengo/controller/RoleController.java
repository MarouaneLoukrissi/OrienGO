package com.example.oriengo.controller;

import com.example.oriengo.model.dto.RoleCreateDTO;
import com.example.oriengo.model.entity.Role;
import com.example.oriengo.service.RoleService;
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
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<Role>> getRoles(){
        List<Role> roles = roleService.getRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id){
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<Role> createRole(@Validated @RequestBody RoleCreateDTO roleInfo){
        Role role = roleService.createRole(roleInfo);
        URI location = URI.create("/api/role/" + role.getId());
        return ResponseEntity.created(location).body(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @Validated @RequestBody RoleCreateDTO roleInfo){
        Role role = roleService.updateRole(id, roleInfo);
        return ResponseEntity.ok(role);
    }
}
