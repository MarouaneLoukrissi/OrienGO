package com.example.oriengo.controller;

import com.example.oriengo.model.dto.UserCreateDTO;
import com.example.oriengo.model.entity.User;
import com.example.oriengo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserContoller {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userService.getUsers(false);
        return ResponseEntity.ok(users);
    }

    @GetMapping("deleted")
    public ResponseEntity<List<User>> getDeletedUsers(){
        List<User> users = userService.getUsers(true);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id, false);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<User> getDeletedUserById(@PathVariable Long id){
        User user = userService.getUserById(id, true);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        User user = userService.getUserByEmail(email, false);
        return ResponseEntity.ok(user);
    }

    @GetMapping("deleted/email/{email}")
    public ResponseEntity<User> getDeletedUserByEmail(@PathVariable String email){
        User user = userService.getUserByEmail(email, true);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role){
        List<User> users = userService.getUsersByRole(role, false);
        return ResponseEntity.ok(users);
    }

    @GetMapping("deleted/role/{role}")
    public ResponseEntity<List<User>> getDeletedUsersByRole(@PathVariable String role){
        List<User> users = userService.getUsersByRole(role, true);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> hardDeleteUser(@PathVariable Long id){
        userService.hardDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Long id){
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Validated @RequestBody UserCreateDTO userInfo){
        User user = userService.createUser(userInfo);
        URI location = URI.create("/api/user/" + user.getId());
        return ResponseEntity.created(location).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Validated @RequestBody UserCreateDTO userInfo){
        User user = userService.updateUser(id, userInfo);
        return ResponseEntity.ok(user);
    }

}
