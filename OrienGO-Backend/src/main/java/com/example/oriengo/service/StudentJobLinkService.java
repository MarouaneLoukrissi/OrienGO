package com.example.oriengo.service;

import com.example.oriengo.model.dto.StudentJobLinkRequestDto;
import com.example.oriengo.model.entity.StudentJobLink;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.model.entity.Job;
import com.example.oriengo.mapper.StudentJobLinkMapper;
import com.example.oriengo.repository.StudentJobLinkRepository;
import com.example.oriengo.repository.StudentRepository;
import com.example.oriengo.repository.JobRepository;
import com.example.oriengo.exception.custom.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentJobLinkService {
    private final StudentJobLinkRepository repository;
    private final StudentRepository studentRepository;
    private final JobRepository jobRepository;
    private final StudentJobLinkMapper mapper;

    public StudentJobLinkService(StudentJobLinkRepository repository,
                                StudentRepository studentRepository,
                                JobRepository jobRepository,
                                StudentJobLinkMapper mapper) {
        this.repository = repository;
        this.studentRepository = studentRepository;
        this.jobRepository = jobRepository;
        this.mapper = mapper;
    }

    public List<StudentJobLink> findAll() {
        return repository.findAll();
    }

    public Optional<StudentJobLink> findById(Long id) {
        return repository.findById(id);
    }

    public StudentJobLink create(StudentJobLinkRequestDto requestDto) {
        Student student = studentRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", requestDto.getStudentId()));
        
        Job job = jobRepository.findById(requestDto.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", requestDto.getJobId()));
        
        StudentJobLink entity = mapper.toEntity(requestDto);
        entity.setStudent(student);
        entity.setJob(job);
        
        return repository.save(entity);
    }

    public StudentJobLink update(Long id, StudentJobLinkRequestDto requestDto) {
        StudentJobLink existingEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StudentJobLink", "id", id));
        
        if (requestDto.getStudentId() != null) {
            Student student = studentRepository.findById(requestDto.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", requestDto.getStudentId()));
            existingEntity.setStudent(student);
        }
        
        if (requestDto.getJobId() != null) {
            Job job = jobRepository.findById(requestDto.getJobId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job", "id", requestDto.getJobId()));
            existingEntity.setJob(job);
        }
        
        mapper.updateEntityFromDto(requestDto, existingEntity);
        return repository.save(existingEntity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<StudentJobLink> findByStudentId(Long studentId) {
        return repository.findByStudentId(studentId);
    }

    public List<StudentJobLink> findByJobId(Long jobId) {
        return repository.findByJobId(jobId);
    }

    public List<StudentJobLink> findByType(String type) {
        return repository.findByType(type);
    }
} 