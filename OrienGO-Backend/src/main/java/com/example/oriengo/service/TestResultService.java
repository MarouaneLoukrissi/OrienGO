package com.example.oriengo.service;

import com.example.oriengo.model.entity.TestResult;
import com.example.oriengo.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TestResultService {

    private final TestResultRepository testResultRepository;

    @Transactional(readOnly = true)
    public List<TestResult> getAll() {
        return testResultRepository.findBySoftDeletedFalse();
    }

    @Transactional(readOnly = true)
    public Optional<TestResult> getById(Long id) {
        return testResultRepository.findById(id)
                .filter(result -> !result.isSoftDeleted());
    }

    @Transactional(readOnly = true)
    public Optional<TestResult> getByTestId(Long testId) {
        return testResultRepository.findByTestIdAndSoftDeletedFalse(testId);
    }

    @Transactional(readOnly = true)
    public List<TestResult> getByStudentId(Long studentId) {
        return testResultRepository.findByStudentIdAndSoftDeletedFalse(studentId);
    }
}
