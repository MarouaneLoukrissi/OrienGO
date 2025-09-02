package com.example.oriengo.controller;

import com.example.oriengo.mapper.StudentMapper;
import com.example.oriengo.model.dto.*;
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
        List<Student> students = studentService.getStudents();
        List<StudentResponseDTO> studentResps = studentMapper.toDTO(students);
        ApiResponse<List<StudentResponseDTO>> response = ApiResponse.<List<StudentResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Students fetched successfully")
                .data(studentResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<List<StudentReturnDTO>>> getStudentsForAdmin() {
        List<Student> students = studentService.getStudents();
        List<StudentReturnDTO> studentResps = studentMapper.toAdminDTO(students);
        ApiResponse<List<StudentReturnDTO>> response = ApiResponse.<List<StudentReturnDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Students fetched successfully for admin")
                .data(studentResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("active")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getActiveStudents(){
        List<Student> students = studentService.getActiveStudents(false);
        List<StudentResponseDTO> studentResps = studentMapper.toDTO(students);
        ApiResponse<List<StudentResponseDTO>> response = ApiResponse.<List<StudentResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Students fetched successfully")
                .data(studentResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/active")
    public ResponseEntity<ApiResponse<List<StudentReturnDTO>>> getActiveStudentsForAdmin(){
        List<Student> students = studentService.getActiveStudents(false);
        List<StudentReturnDTO> studentResps = studentMapper.toAdminDTO(students);
        ApiResponse<List<StudentReturnDTO>> response = ApiResponse.<List<StudentReturnDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Students fetched successfully")
                .data(studentResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/average-profiles")
    public ResponseEntity<ApiResponse<TestResultProfilesDTO>> getAverageProfilesByDeleted(
            @RequestParam(defaultValue = "false") boolean deleted) {
        TestResultProfilesDTO averageProfiles = studentService.getAverageProfilesByDeleted(deleted);

        ApiResponse<TestResultProfilesDTO> response = ApiResponse.<TestResultProfilesDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Average profiles fetched successfully")
                .data(averageProfiles)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted")
    public ResponseEntity<ApiResponse<List<StudentReturnDTO>>> getDeletedStudents(){
        List<Student> students = studentService.getActiveStudents(true);
        List<StudentReturnDTO> studentResps = studentMapper.toAdminDTO(students);
        ApiResponse<List<StudentReturnDTO>> response = ApiResponse.<List<StudentReturnDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted students fetched successfully")
                .data(studentResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/deleted")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getDeletedStudentsForAdmin(){
        List<Student> students = studentService.getActiveStudents(true);
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

    @GetMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<StudentReturnDTO>> getStudentByIdForAdmin(@PathVariable Long id){
        Student student = studentService.getStudentById(id, false);
        StudentReturnDTO studentResp = studentMapper.toAdminDTO(student);
        ApiResponse<StudentReturnDTO> response = ApiResponse.<StudentReturnDTO>builder()
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

    @GetMapping("/admin/deleted/{id}")
    public ResponseEntity<ApiResponse<StudentReturnDTO>> getDeletedStudentByIdForAdmin(@PathVariable Long id){
        Student student = studentService.getStudentById(id, true);
        StudentReturnDTO studentResp = studentMapper.toAdminDTO(student);
        ApiResponse<StudentReturnDTO> response = ApiResponse.<StudentReturnDTO>builder()
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

    @GetMapping("/admin/email/{email}")
    public ResponseEntity<ApiResponse<StudentReturnDTO>> getStudentByEmailForAdmin(@PathVariable String email){
        Student student = studentService.getStudentByEmail(email, false);
        StudentReturnDTO studentResp = studentMapper.toAdminDTO(student);
        ApiResponse<StudentReturnDTO> response = ApiResponse.<StudentReturnDTO>builder()
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

    @GetMapping("/admin/deleted/email/{email}")
    public ResponseEntity<ApiResponse<StudentReturnDTO>> getDeletedStudentByEmailForAdmin(@PathVariable String email){
        Student student = studentService.getStudentByEmail(email, true);
        StudentReturnDTO studentResp = studentMapper.toAdminDTO(student);
        ApiResponse<StudentReturnDTO> response = ApiResponse.<StudentReturnDTO>builder()
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

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse<StudentReturnDTO>> createStudentForAdmin(@Valid @RequestBody StudentDTO studentInfo){
        Student student = studentService.createStudentForAdmin(studentInfo);
        StudentReturnDTO studentResp = studentMapper.toAdminDTO(student);
        ApiResponse<StudentReturnDTO> response = ApiResponse.<StudentReturnDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Student created successfully")
                .data(studentResp)
                .build();
        URI location = URI.create("/api/student/" + student.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateProfile(@PathVariable Long id, @Valid @RequestBody StudentUpdateDTO studentInfo){
        Student student = studentService.updateProfile(id, studentInfo);
        StudentResponseDTO studentResp = studentMapper.toDTO(student);
        ApiResponse<StudentResponseDTO> response = ApiResponse.<StudentResponseDTO>builder()
                .code("STUDENT_UPDATED")
                .status(200)
                .message("Student updated successfully")
                .data(studentResp)
                .build();
        return ResponseEntity.ok(response);
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

    @PutMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<StudentReturnDTO>> updateStudentForAdmin(@PathVariable Long id, @Valid @RequestBody StudentModifyDTO studentInfo){
        Student student = studentService.updateStudentForAdmin(id, studentInfo);
        StudentReturnDTO studentResp = studentMapper.toAdminDTO(student);
        ApiResponse<StudentReturnDTO> response = ApiResponse.<StudentReturnDTO>builder()
                .code("STUDENT_UPDATED")
                .status(200)
                .message("Student updated successfully")
                .data(studentResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
