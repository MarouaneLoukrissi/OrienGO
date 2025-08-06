package com.example.oriengo.service;

import com.example.oriengo.mapper.StudentMapper;
import com.example.oriengo.mapper.TestMapper;
import com.example.oriengo.model.dto.StudentDTO;
import com.example.oriengo.model.dto.StudentResponseDTO;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.repository.StudentRepository;
import com.example.oriengo.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository repo;
    private final StudentMapper studentMapper;

    @Transactional
    public StudentResponseDTO create(StudentDTO dto) {
        Student student = studentMapper.toEntity(dto);
        Student saved = repo.save(student);
        return studentMapper.toResponseDto(saved);
    }
    private final TestRepository testRepository;
    private final StudentRepository studentRepository;
    private final TestMapper testMapper;



    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAll() {
        return repo.findAll().stream()
                .map(studentMapper::toResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public java.util.Optional<StudentResponseDTO> getById(Long id) {
        return repo.findById(id).map(studentMapper::toResponseDto);
    }

    @Transactional
    public StudentResponseDTO update(Long id, StudentDTO dto) {
        return repo.findById(id)
                .map(existing -> {
                    // appliquer les changements du DTO
                    Student updated = studentMapper.toEntity(dto);
                    // garder l'id et éventuelles relations non sobrescrites
                    updated.setId(existing.getId());
                    Student saved = repo.save(updated);
                    return studentMapper.toResponseDto(saved);
                })
                .orElseThrow(() -> new RuntimeException("Student not found with id " + id));
    }

    @Transactional
    public boolean delete(Long id) {
        return repo.findById(id).map(s -> {
            repo.delete(s);
            return true;
        }).orElse(false);
    }


}

