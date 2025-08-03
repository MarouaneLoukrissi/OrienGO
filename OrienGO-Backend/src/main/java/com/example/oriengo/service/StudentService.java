package com.example.oriengo.service;

import com.example.oriengo.exception.PathVarException;
import com.example.oriengo.exception.user.UserCreationException;
import com.example.oriengo.exception.user.UserDeleteException;
import com.example.oriengo.exception.user.UserGetException;
import com.example.oriengo.exception.user.UserUpdateException;
import com.example.oriengo.mapper.StudentMapper;
import com.example.oriengo.model.dto.StudentCreateDTO;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.repository.RoleRepository;
import com.example.oriengo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final StudentMapper studentMapper;
//    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<Student> getStudents(boolean deleted) {
        try{
            return studentRepository.findByIsDeleted(deleted);
        } catch (Exception e){
            throw new UserGetException(HttpStatus.NOT_FOUND, "No Student found");
        }
    }

    public Student getStudentById(Long id, boolean deleted) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Student ID cannot be empty");
        }
        return studentRepository.findByIdAndIsDeleted(id, deleted)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public Student getStudentById(Long id) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Student ID cannot be empty");
        }
        return studentRepository.findById(id)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public Student getStudentByEmail(String email, boolean deleted) {
        if (email == null || email.trim().isEmpty()) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Student Email cannot be empty");
        }
        return studentRepository.findByEmailAndIsDeleted(email, deleted)
                .orElseThrow(() -> new UserGetException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public void hardDeleteStudent(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student ID cannot be empty");
        }
        try{
            Student student = getStudentById(id);
            studentRepository.deleteById(student.getId());
            log.info("Student hard deleted with ID: {}", student.getId());
        } catch (Exception e) {
            log.error("Error hard deleting student: {}", e.getMessage());
            throw new UserDeleteException("Failed to hard delete student");
        }
    }

    public void softDeleteStudent(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student ID cannot be empty");
        }
        try{
            Student student = getStudentById(id, false);
            student.setDeleted(true);
            student.setDeletedAt(LocalDateTime.now());
            student.setEmail("deleted_" + UUID.randomUUID() + "_" + student.getEmail());
            studentRepository.save(student);
            log.info("Student soft deleted with ID: {}", student.getId());
        } catch (Exception e) {
            log.error("Error soft deleting student: {}", e.getMessage());
            throw new UserDeleteException("Failed to soft delete student");
        }
    }

    public Student createStudent(StudentCreateDTO dto) {
        try {
            Student student = studentMapper.toEntity(dto);
            student.setPassword(student.getPassword()); //encoder.encode(student.getPassword())
            Student studentOutput = studentRepository.save(student);
            log.info("Student created with ID: {}", studentOutput.getId());
            return studentOutput;
        } catch (Exception e) {
            log.error("Error creating student: {}", e.getMessage());
            throw new UserCreationException("Failed to create student");
        }
    }

    public Student updateStudent(Long id, StudentCreateDTO dto) {
        if (id == null) {
            throw new PathVarException(HttpStatus.BAD_REQUEST, "Student ID cannot be empty");
        }
        try {
            Student student = studentMapper.toEntity(dto);
            Student studentChecked = getStudentById(id);
            student.setId(studentChecked.getId());
            student.setPassword(student.getPassword()); //encoder.encode(student.getPassword())
            Student studentOutput = studentRepository.save(student);
            log.info("Student updated with ID: {}", studentOutput.getId());
            return studentOutput;
        } catch (Exception e) {
            log.error("Error updating student: {}", e.getMessage());
            throw new UserUpdateException("Failed to update student");
        }
    }
}
