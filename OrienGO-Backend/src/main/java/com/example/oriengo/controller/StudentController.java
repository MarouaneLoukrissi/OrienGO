package com.example.oriengo.controller;

import com.example.oriengo.mapper.StudentMapper;
import com.example.oriengo.model.dto.StudentCreateDTO;
import com.example.oriengo.model.dto.StudentResponseDTO;
import com.example.oriengo.model.entity.Admin;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.StudentService;
import jakarta.validation.Valid;
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
@Validated
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getStudents(){
        List<Student> students = studentService.getStudents(false);
        List<StudentResponseDTO> studentResps = studentMapper.toDTO(students);
        ApiResponse<List<StudentResponseDTO>> response = ApiResponse.<List<StudentResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Students fetched successfully")
                .data(studentResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getDeletedStudents(){
        List<Student> students = studentService.getStudents(true);
        List<StudentResponseDTO> studentResps = studentMapper.toDTO(students);
        ApiResponse<List<StudentResponseDTO>> response = ApiResponse.<List<StudentResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted students fetched successfully")
                .data(studentResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(@PathVariable Long id){
        Student student = studentService.getStudentById(id, false);
        StudentResponseDTO studentResp = studentMapper.toDTO(student);
        ApiResponse<StudentResponseDTO> response = ApiResponse.<StudentResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Student fetched successfully")
                .data(studentResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getDeletedStudentById(@PathVariable Long id){
        Student student = studentService.getStudentById(id, true);
        StudentResponseDTO studentResp = studentMapper.toDTO(student);
        ApiResponse<StudentResponseDTO> response = ApiResponse.<StudentResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted student fetched successfully")
                .data(studentResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentByEmail(@PathVariable String email){
        Student student = studentService.getStudentByEmail(email, false);
        StudentResponseDTO studentResp = studentMapper.toDTO(student);
        ApiResponse<StudentResponseDTO> response = ApiResponse.<StudentResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Student fetched successfully")
                .data(studentResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted/email/{email}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getDeletedStudentByEmail(@PathVariable String email){
        Student student = studentService.getStudentByEmail(email, true);
        StudentResponseDTO studentResp = studentMapper.toDTO(student);
        ApiResponse<StudentResponseDTO> response = ApiResponse.<StudentResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted student fetched successfully")
                .data(studentResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteStudent(@PathVariable Long id){
        studentService.hardDeleteStudent(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("STUDENT_DELETED")
                .status(204)
                .message("Student hard deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteStudent(@PathVariable Long id){
        studentService.softDeleteStudent(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("SUCCESS")
                .status(204)
                .message("Student soft deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> restoreStudent(@PathVariable Long id) {
        Student student = studentService.restoreStudent(id);
        StudentResponseDTO studentResp = studentMapper.toDTO(student);
        ApiResponse<StudentResponseDTO> response = ApiResponse.<StudentResponseDTO>builder()
                .code("STUDENT_RESTORED")
                .status(200)
                .message("Student restored successfully")
                .data(studentResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(@Valid @RequestBody StudentCreateDTO studentInfo){
        Student student = studentService.createStudent(studentInfo);
        StudentResponseDTO studentResp = studentMapper.toDTO(student);
        ApiResponse<StudentResponseDTO> response = ApiResponse.<StudentResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Student created successfully")
                .data(studentResp)
                .build();
        URI location = URI.create("/api/student/" + student.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentCreateDTO studentInfo){
        Student student = studentService.updateStudent(id, studentInfo);
        StudentResponseDTO studentResp = studentMapper.toDTO(student);
        ApiResponse<StudentResponseDTO> response = ApiResponse.<StudentResponseDTO>builder()
                .code("STUDENT_UPDATED")
                .status(200)
                .message("Student updated successfully")
                .data(studentResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
