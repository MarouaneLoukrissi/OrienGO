package com.example.oriengo.controller;

import com.example.oriengo.model.dto.StudentCreateDTO;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getStudents(){
        List<Student> students = studentService.getStudents(false);
        return ResponseEntity.ok(students);
    }

    @GetMapping("deleted")
    public ResponseEntity<List<Student>> getDeletedStudents(){
        List<Student> students = studentService.getStudents(true);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id){
        Student student = studentService.getStudentById(id, false);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<Student> getDeletedStudentById(@PathVariable Long id){
        Student student = studentService.getStudentById(id, true);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Student> getStudentByEmail(@PathVariable String email){
        Student student = studentService.getStudentByEmail(email, false);
        return ResponseEntity.ok(student);
    }

    @GetMapping("deleted/email/{email}")
    public ResponseEntity<Student> getDeletedStudentByEmail(@PathVariable String email){
        Student student = studentService.getStudentByEmail(email, true);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> hardDeleteStudent(@PathVariable Long id){
        studentService.hardDeleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteStudent(@PathVariable Long id){
        studentService.softDeleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@Validated @RequestBody StudentCreateDTO studentInfo){
        Student student = studentService.createStudent(studentInfo);
        URI location = URI.create("/api/student/" + student.getId());
        return ResponseEntity.created(location).body(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Validated @RequestBody StudentCreateDTO studentInfo){
        Student student = studentService.updateStudent(id, studentInfo);
        return ResponseEntity.ok(student);
    }
}
