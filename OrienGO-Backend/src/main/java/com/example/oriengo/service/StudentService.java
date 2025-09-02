package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.user.*;
import com.example.oriengo.mapper.StudentMapper;
import com.example.oriengo.model.dto.*;
import com.example.oriengo.model.entity.Role;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.model.enumeration.Category;
import com.example.oriengo.repository.RoleRepository;
import com.example.oriengo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final RoleRepository roleRepository;
//    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final MessageSource messageSource;
    private final TestResultService testResultService;

    private String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
    }

    public List<Student> getStudents() {
        try {
            log.info("Fetching students");
            List<Student> students = studentRepository.findAll();
            log.info("Found {} students", students.size());
            return students;
        } catch (Exception e) {
            log.error("Failed to fetch students: {}", e.getMessage(), e);
            throw new UserGetException(HttpStatus.NOT_FOUND, getMessage("student.not.found"));
        }
    }

    public List<Student> getActiveStudents(boolean deleted) {
        try {
            log.info("Fetching students with isDeleted = {}", deleted);
            List<Student> students = studentRepository.findByIsDeleted(deleted);
            log.info("Found {} students", students.size());
            return students;
        } catch (Exception e) {
            log.error("Failed to fetch students: {}", e.getMessage(), e);
            throw new UserGetException(HttpStatus.NOT_FOUND, getMessage("student.not.found"));
        }
    }

    @Transactional(readOnly = true)
    public TestResultProfilesDTO getAverageProfilesByDeleted(boolean deleted) {
        try {
            log.info("Fetching student IDs with isDeleted = {}", deleted);

            // Get student IDs instead of full student entities
            List<Long> studentIds = studentRepository.findIdsByIsDeleted(deleted);

            if (studentIds.isEmpty()) {
                log.warn("No students found with isDeleted = {}", deleted);

                // Return all categories with 0.0 if no students found
                List<ProfileScoreDTO> emptyProfiles = Arrays.stream(Category.values())
                        .map(cat -> new ProfileScoreDTO(cat, 0.0))
                        .collect(Collectors.toList());

                return TestResultProfilesDTO.builder()
                        .profiles(emptyProfiles)
                        .build();
            }

            // Calculate average profiles across studentIds
            TestResultProfilesDTO averageProfiles = testResultService.getAverageProfilesByStudentIds(studentIds);

            // Ensure profiles are sorted descending
            List<ProfileScoreDTO> sortedProfiles = averageProfiles.getProfiles().stream()
                    .sorted(Comparator.comparing(ProfileScoreDTO::getPercentage).reversed())
                    .collect(Collectors.toList());

            averageProfiles.setProfiles(sortedProfiles);

            log.info("Successfully calculated average profiles for {} students", studentIds.size());
            return averageProfiles;

        } catch (Exception e) {
            log.error("Failed to calculate average profiles: {}", e.getMessage(), e);
            throw new UserGetException(HttpStatus.NOT_FOUND, getMessage("student.not.found"));
        }
    }

    public Student getStudentById(Long id, boolean deleted) {
        if (id == null) {
            log.warn("Attempted to fetch student with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("student.id.empty"));
        }
        return studentRepository.findByIdAndIsDeleted(id, deleted)
                .orElseThrow(() -> {
                    log.error("Student not found with ID: {}", id);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("student.not.found"));
                });
    }

    private Student getStudentById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch student with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("student.id.empty"));
        }
        return studentRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> {
                    log.error("Student not found with ID: {}", id);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("student.not.found"));
                });
    }

    public Student getStudentByEmail(String email, boolean deleted) {
        if (email == null || email.trim().isEmpty()) {
            log.warn("Attempted to fetch student with null or empty email");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("student.email.empty"));
        }
        return studentRepository.findByEmailAndIsDeleted(email, deleted)
                .orElseThrow(() -> {
                    log.error("Student not found with email: {}", email);
                    return new UserGetException(HttpStatus.NOT_FOUND, getMessage("student.not.found"));
                });
    }

    @Transactional
    public void hardDeleteStudent(Long id) {
        if (id == null) {
            log.warn("Attempted hard delete with null student ID");
            throw new UserDeleteException(HttpStatus.BAD_REQUEST, getMessage("student.id.empty"));
        }
        try {
            Student student = getStudentById(id);
            studentRepository.deleteById(student.getId());
            log.info("Successfully hard deleted student with ID: {}", student.getId());
        } catch (Exception e) {
            log.error("Error during hard delete of student with ID {}: {}", id, e.getMessage(), e);
            throw new UserDeleteException(HttpStatus.CONFLICT, getMessage("student.hard.delete.failed"));
        }
    }

    @Transactional
    public void softDeleteStudent(Long id) {
        if (id == null) {
            log.warn("Attempted soft delete with null student ID");
            throw new UserDeleteException(HttpStatus.BAD_REQUEST, getMessage("student.id.empty"));
        }
        try {
            Student student = getStudentById(id, false);
            String originalEmail = student.getEmail();

            student.setDeleted(true);
            student.setDeletedAt(LocalDateTime.now());
            student.setEmail("deleted_" + UUID.randomUUID() + "_" + originalEmail);

            studentRepository.save(student);

            log.info("Successfully soft deleted student with ID: {}", student.getId());
        } catch (Exception e) {
            log.error("Error during soft delete of student with ID {}: {}", id, e.getMessage(), e);
            throw new UserDeleteException(HttpStatus.CONFLICT, getMessage("student.soft.delete.failed"));
        }
    }

    @Transactional
    public Student restoreStudent(Long id) {
        if (id == null) {
            log.warn("Attempted restore with null student ID");
            throw new UserRestoreException(HttpStatus.BAD_REQUEST, getMessage("student.id.empty"));
        }

        try {
            log.info("Attempting restore for student with ID: {}", id);

            Student student = getStudentById(id, true); // fetch soft-deleted student
            String deletedEmail = student.getEmail();

            // Extract original email from deleted email format "deleted_<UUID>_originalEmail"
            String prefix = "deleted_";
            int index = deletedEmail.indexOf("_", prefix.length()); // find the second underscore
            if (index == -1 || index + 1 >= deletedEmail.length()) {
                throw new UserRestoreException(HttpStatus.CONFLICT, getMessage("student.restore.email.invalid"));
            }

            String originalEmail = deletedEmail.substring(index + 1);

            // Check if original email is already used by a non-deleted student
            boolean emailTaken = studentRepository.existsByEmailAndIsDeletedFalse(originalEmail);
            if (emailTaken) {
                log.warn("Original email {} is already in use. Cannot restore to original email.", originalEmail);
                throw new UserRestoreException(HttpStatus.CONFLICT, getMessage("student.restore.email.used"));
            }

            student.setDeleted(false);
            student.setDeletedAt(null);
            student.setEmail(originalEmail); // restore original email

            studentRepository.save(student);

            log.info("Successfully restored student with ID: {}", student.getId());
            return student;
        } catch (Exception e) {
            log.error("Error during restore of student with ID {}: {}", id, e.getMessage(), e);
            throw new UserRestoreException(HttpStatus.BAD_REQUEST, getMessage("student.restore.failed"));
        }
    }

    @Transactional
    public Student createStudent(StudentCreateDTO dto) {
        if (dto == null) {
            log.warn("Student request cannot be null");
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("student.dto.empty"));
        }
        try {
            log.info("Starting creation of new student with email: {}", dto.getEmail());

            // Check if email already exists
            try {
                Student existingStudent = getStudentByEmail(dto.getEmail(), false);
                if (existingStudent != null) {
                    log.warn("Student already exists with email: {}", dto.getEmail());
                    throw new UserCreationException(HttpStatus.CONFLICT, getMessage("student.email.already.exists", dto.getEmail()));
                }
            } catch (UserGetException e) {
                log.debug("No existing student found with email {}, proceeding with creation", dto.getEmail());
            }

            Student student = studentMapper.toEntity(dto);
            student.setPassword(student.getPassword()); //encoder.encode(student.getPassword())
//            student.setEnabled(true);

            String roleName = "STUDENT";
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> {
                        log.warn("Role '{}' not found in database", roleName);
                        return new UserCreationException(HttpStatus.NOT_FOUND , getMessage("student.role.not.found", roleName));
                    });

            student.setRoles(Set.of(role));

            Student savedStudent = studentRepository.save(student);
            log.info("Student created successfully with ID: {}", savedStudent.getId());

            return savedStudent;

        } catch (UserCreationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during student creation for email {}: {}", dto.getEmail(), e.getMessage(), e);
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("student.create.failed"));
        }
    }

    @Transactional
    public Student createStudentForAdmin(StudentDTO dto) {
        if (dto == null) {
            log.warn("Student request cannot be null");
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("student.dto.empty"));
        }

        try {
            log.info("Starting creation of new student with email: {}", dto.getEmail());

            // Check if email already exists
            try {
                Student existingStudent = getStudentByEmail(dto.getEmail(), false);
                if (existingStudent != null) {
                    log.warn("Student already exists with email: {}", dto.getEmail());
                    throw new UserCreationException(HttpStatus.CONFLICT,
                            getMessage("student.email.already.exists", dto.getEmail()));
                }
            } catch (UserGetException e) {
                log.debug("No existing student found with email {}, proceeding with creation", dto.getEmail());
            }

            // Map DTO to Entity
            Student student = studentMapper.toEntity(dto);

            // Password encoding
            student.setPassword(student.getPassword()); // replace with encoder.encode(dto.getPassword()) if needed

            // Set admin-controlled fields
            student.setEnabled(dto.isEnabled());
            student.setSuspended(dto.isSuspended());
            student.setSuspensionReason(dto.getSuspensionReason());
            student.setSuspendedUntil(dto.getSuspendedUntil());

            // Assign default role
            String roleName = "STUDENT";
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> {
                        log.warn("Role '{}' not found in database", roleName);
                        return new UserCreationException(HttpStatus.NOT_FOUND,
                                getMessage("student.role.not.found", roleName));
                    });
            student.setRoles(Set.of(role));

            // Save student
            Student savedStudent = studentRepository.save(student);
            log.info("Student created successfully with ID: {}", savedStudent.getId());

            return savedStudent;

        } catch (UserCreationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during student creation for email {}: {}", dto.getEmail(), e.getMessage(), e);
            throw new UserCreationException(HttpStatus.BAD_REQUEST, getMessage("student.create.failed"));
        }
    }

    @Transactional
    public Student updateProfile(Long id, StudentUpdateDTO dto) {
        if (id == null) {
            log.warn("Attempted to update student with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("student.id.empty"));
        } else if (dto == null) {
            log.warn("Student update request cannot be null");
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("student.dto.empty"));
        }
        try {
            log.info("Updating student with ID: {}", id);
            Student existingStudent = getStudentById(id, false);
            studentMapper.updateStudentFromDto(dto, existingStudent);
            Student savedStudent = studentRepository.save(existingStudent);
            log.info("Student with ID {} successfully updated", savedStudent.getId());
            return savedStudent;
        } catch (Exception e) {
            log.error("Error updating student with ID {}: {}", id, e.getMessage(), e);
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("student.update.failed"));
        }
    }
    @Transactional
    public Student updateStudent(Long id, StudentCreateDTO dto) {
        if (id == null) {
            log.warn("Attempted to update student with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("student.id.empty"));
        } else if (dto == null) {
            log.warn("Student update request cannot be null");
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("student.dto.empty"));
        }
        try {
            log.info("Updating student with ID: {}", id);
            // Check if email already exists
            try {
                Student existingStudent = getStudentByEmail(dto.getEmail(), false);
                if (existingStudent != null) {
                    log.warn("Student already exists with email: {}", dto.getEmail());
                    throw new UserCreationException(HttpStatus.CONFLICT, getMessage("student.email.already.exists", dto.getEmail()));
                }
            } catch (UserGetException e) {
                log.debug("No existing student found with email {}, proceeding with creation", dto.getEmail());
            }
            Student existingStudent = getStudentById(id);
            studentMapper.updateStudentFromDto(dto, existingStudent);
            existingStudent.setPassword(existingStudent.getPassword()); //encoder.encode(student.getPassword())
            Student savedStudent = studentRepository.save(existingStudent);
            log.info("Student with ID {} successfully updated", savedStudent.getId());
            return savedStudent;
        } catch (Exception e) {
            log.error("Error updating student with ID {}: {}", id, e.getMessage(), e);
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("student.update.failed"));
        }
    }
    @Transactional
    public Student updateStudentForAdmin(Long id, StudentModifyDTO dto) {
        if (id == null) {
            log.warn("Attempted to update student with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("student.id.empty"));
        }
        if (dto == null) {
            log.warn("Student update request cannot be null");
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("student.dto.empty"));
        }

        try {
            log.info("Updating student with ID: {}", id);

            // Fetch existing student
            Student existingStudent = getStudentById(id);

            // Map fields from DTO to entity using MapStruct
            studentMapper.updateStudentFromDto(dto, existingStudent);

            // Encode password if needed
            existingStudent.setPassword(existingStudent.getPassword()); // replace with encoder.encode(dto.getPassword()) if you use encoding

            // Save updated student
            Student savedStudent = studentRepository.save(existingStudent);
            log.info("Student with ID {} successfully updated", savedStudent.getId());

            return savedStudent;
        } catch (Exception e) {
            log.error("Error updating student with ID {}: {}", id, e.getMessage(), e);
            throw new UserUpdateException(HttpStatus.BAD_REQUEST, getMessage("student.update.failed"));
        }
    }

}
