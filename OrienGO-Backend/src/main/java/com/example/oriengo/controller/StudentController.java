package com.example.oriengo.controller;

import com.example.oriengo.model.dto.StudentDTO;
import com.example.oriengo.model.dto.StudentResponseDTO;
import com.example.oriengo.model.dto.StudentResponseDTO;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.service.StudentService;
import com.example.oriengo.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentRepository repo;

    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Student > create(@RequestBody Student student) {
        Student saved = repo.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
