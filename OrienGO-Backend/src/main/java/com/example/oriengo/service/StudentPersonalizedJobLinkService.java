package com.example.oriengo.service;

import com.example.oriengo.model.entity.StudentPersonalizedJobLink;
import com.example.oriengo.repository.StudentPersonalizedJobLinkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentPersonalizedJobLinkService {
    private final StudentPersonalizedJobLinkRepository repository;

    public StudentPersonalizedJobLinkService(StudentPersonalizedJobLinkRepository repository) {
        this.repository = repository;
    }

    public List<StudentPersonalizedJobLink> findAll() {
        return repository.findAll();
    }

    public Optional<StudentPersonalizedJobLink> findById(Long id) {
        return repository.findById(id);
    }

    public StudentPersonalizedJobLink save(StudentPersonalizedJobLink link) {
        return repository.save(link);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}