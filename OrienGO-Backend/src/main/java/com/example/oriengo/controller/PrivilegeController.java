package com.example.oriengo.controller;

import com.example.oriengo.model.dto.PrivilegeCreateDTO;
import com.example.oriengo.model.entity.Privilege;
import com.example.oriengo.service.PrivilegeService;
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
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    @GetMapping
    public ResponseEntity<List<Privilege>> getPrivileges(){
        List<Privilege> privileges = privilegeService.getPrivileges();
        return ResponseEntity.ok(privileges);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Privilege> getPrivilegeById(@PathVariable Long id){
        Privilege privilege = privilegeService.getPrivilegeById(id);
        return ResponseEntity.ok(privilege);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<Privilege> getPrivilegeById(@PathVariable String name) {
        Privilege privilege = privilegeService.getPrivilegeByName(name);
        return ResponseEntity.ok(privilege);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrivilege(@PathVariable Long id){
        privilegeService.deletePrivilege(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<Privilege> createPrivilege(@Validated @RequestBody PrivilegeCreateDTO privilegeInfo){
        Privilege privilege = privilegeService.createPrivilege(privilegeInfo);
        URI location = URI.create("/api/privilege/" + privilege.getId());
        return ResponseEntity.created(location).body(privilege);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Privilege> updatePrivilege(@PathVariable Long id, @Validated @RequestBody PrivilegeCreateDTO privilegeInfo){
        Privilege privilege = privilegeService.updatePrivilege(id, privilegeInfo);
        return ResponseEntity.ok(privilege);
    }
}
