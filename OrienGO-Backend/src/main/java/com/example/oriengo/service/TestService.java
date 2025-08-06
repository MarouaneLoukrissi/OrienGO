package com.example.oriengo.service;

import com.example.oriengo.mapper.TestMapper;
import com.example.oriengo.model.dto.TestDTO;

import com.example.oriengo.model.entity.Student;
import com.example.oriengo.model.entity.Test;
import com.example.oriengo.model.enumeration.TestStatus;
import com.example.oriengo.repository.StudentRepository;
import com.example.oriengo.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import com.example.oriengo.model.entity.Question;
import com.example.oriengo.model.enumeration.TestType;
import com.example.oriengo.repository.QuestionRepository;
import com.example.oriengo.mapper.StudentMapper;
import com.example.oriengo.model.dto.StudentDTO;
import com.example.oriengo.model.entity.TestResult;
import com.example.oriengo.model.enumeration.Category;
import com.example.oriengo.repository.TestResultRepository;

import java.util.Map;
import java.util.EnumMap;
import com.example.oriengo.model.dto.QuestionDTO;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class TestService {

    private final TestRepository testRepository;
    private final StudentRepository studentRepository;
    private final TestMapper testMapper;
    private final QuestionRepository questionRepository;
    private final StudentMapper studentMapper;
    private final TestResultRepository testResultRepository;

    @Transactional(readOnly = true)
    public List<TestDTO> getAll() {
        return testRepository.findAllWithRelations()
                .stream()
                .map(this::mapTestToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<TestDTO> getById(Long id) {
        return testRepository.findByIdWithRelations(id)
                .map(this::mapTestToDTO);
    }

    private TestDTO mapTestToDTO(Test test) {
        TestDTO testDTO = new TestDTO();
        
        // Mapper tous les champs manuellement
        testDTO.setId(test.getId());
        testDTO.setType(test.getType());
        testDTO.setStatus(test.getStatus());
        testDTO.setStartedAt(test.getStartedAt() != null ? test.getStartedAt().toString() : null);
        testDTO.setCompletedAt(test.getCompletedAt() != null ? test.getCompletedAt().toString() : null);
        testDTO.setDurationMinutes(test.getDurationMinutes());
        testDTO.setQuestionsCount(test.getQuestionsCount());
        testDTO.setSoftDeleted(test.isSoftDeleted());
        testDTO.setStudentId(test.getStudent() != null ? test.getStudent().getId() : null);
        
        // Mapper les questions
        List<QuestionDTO> questionDTOs = test.getQuestions().stream()
                .map(q -> {
                    QuestionDTO qDto = new QuestionDTO();
                    qDto.setId(q.getId());
                    qDto.setCategory(q.getCategory());
                    qDto.setText(q.getText());
                    return qDto;
                })
                .collect(Collectors.toList());
        testDTO.setQuestions(questionDTOs);

        return testDTO;
    }


    @Transactional
    public TestDTO createStudentAndTestWithRandomQuestions(StudentDTO studentDto, TestType type) {
        // 1. Créer le Student
        Student student = studentMapper.toEntity(studentDto);
        student = studentRepository.save(student);

        // 2. Tirage aléatoire des questions
        List<Question> randomQuestions;
        if (type == TestType.FAST) {
            // Pour FAST : 3 questions par catégorie RIASEC (6 catégories × 3 = 18 questions)
            randomQuestions = new ArrayList<>();
            for (Category category : Category.values()) {
                List<Question> categoryQuestions = questionRepository.findRandomQuestionsByCategory(category.name(), 3);
                randomQuestions.addAll(categoryQuestions);
            }
        } else {
            // Pour COMPLETE : 10 questions par catégorie RIASEC (6 catégories × 10 = 60 questions)
            randomQuestions = new ArrayList<>();
            for (Category category : Category.values()) {
                List<Question> categoryQuestions = questionRepository.findRandomQuestionsByCategory(category.name(), 10);
                randomQuestions.addAll(categoryQuestions);
            }
        }

        // 3. Créer le Test et lier à l'étudiant
        Test test = new Test();
        test.setType(type);
        test.setStatus(TestStatus.PENDING);
        test.setStartedAt(LocalDateTime.now());
        test.setStudent(student);
        test.setQuestions(new HashSet<>(randomQuestions));
        test.setQuestionsCount(randomQuestions.size());
        test.setDurationMinutes(0);
        test.setSoftDeleted(false);

        test = testRepository.save(test);

        // 4. Ajouter le test à la collection de l'étudiant
        student.getTests().add(test);
        studentRepository.save(student);

        // 5. Retourner le TestDTO avec tous les champs mappés manuellement
        TestDTO testDTO = new TestDTO();
        
        // Mapper tous les champs manuellement
        testDTO.setId(test.getId());
        testDTO.setType(test.getType());
        testDTO.setStatus(test.getStatus());
        testDTO.setStartedAt(test.getStartedAt() != null ? test.getStartedAt().toString() : null);
        testDTO.setCompletedAt(test.getCompletedAt() != null ? test.getCompletedAt().toString() : null);
        testDTO.setDurationMinutes(test.getDurationMinutes());
        testDTO.setQuestionsCount(test.getQuestionsCount());
        testDTO.setSoftDeleted(test.isSoftDeleted());
        testDTO.setStudentId(student.getId());
        
        // Mapper les questions
        List<QuestionDTO> questionDTOs = test.getQuestions().stream()
                .map(q -> {
                    QuestionDTO qDto = new QuestionDTO();
                    qDto.setId(q.getId());
                    qDto.setCategory(q.getCategory());
                    qDto.setText(q.getText());
                    return qDto;
                })
                .collect(Collectors.toList());
        testDTO.setQuestions(questionDTOs);

        return testDTO;
    }


    @Transactional
    public Optional<TestDTO> completeTest(Long id, TestDTO dto) {
        return testRepository.findById(id)
                .filter(t -> !t.isSoftDeleted())
                .map(existing -> {
                    existing.setStatus(TestStatus.COMPLETED);
                    existing.setCompletedAt(LocalDateTime.now());
                    if (dto.getDurationMinutes() != null) {
                        existing.setDurationMinutes(dto.getDurationMinutes());
                    }
                    if (dto.getQuestionsCount() != null) {
                        existing.setQuestionsCount(dto.getQuestionsCount());
                    }
                    Test updated = testRepository.save(existing);
                    return testMapper.toDto(updated);
                });
    }

    @Transactional
    public Optional<TestDTO> cancelTest(Long id) {
        return testRepository.findById(id)
                .filter(t -> !t.isSoftDeleted())
                .map(existing -> {
                    existing.setStatus(TestStatus.CANCELLED);
                    Test updated = testRepository.save(existing);
                    return testMapper.toDto(updated);
                });
    }

    @Transactional
    public boolean softDelete(Long id) {
        return testRepository.findById(id)
                .filter(t -> !t.isSoftDeleted())
                .map(t -> {
                    t.setSoftDeleted(true);
                    testRepository.save(t);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public TestResult submitTestAndCalculateResults(Long testId, Map<Long, Integer> answers) {
        //  Récupérer le test avec ses questions
        Test test = testRepository.findByIdWithRelations(testId)
                .orElseThrow(() -> new RuntimeException("Test not found with id " + testId));

        //  Calculer les scores par catégorie RIASEC
        Map<Category, Integer> scores = calculateRIASECScores(test.getQuestions(), answers);

        //  Déterminer le type dominant
        Category dominantType = scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Category.REALISTIC);

        // Créer le TestResult
        TestResult testResult = TestResult.builder()
                .test(test)
                .dominantType(dominantType)
                .dominantTypeDescription(dominantType.toString())
                .scores(scores)
                .keyPoints("Key points based on your " + dominantType + " profile")
                .shared(false)
                .downloaded(false)
                .softDeleted(false)
                .build();

        //  Sauvegarder le TestResult
        testResult = testResultRepository.save(testResult);

        //  Mettre à jour le statut du test
        test.setStatus(TestStatus.COMPLETED);
        test.setCompletedAt(LocalDateTime.now());
        testRepository.save(test);

        return testResult;
    }

    private Map<Category, Integer> calculateRIASECScores(Set<Question> questions, Map<Long, Integer> answers) {
        Map<Category, Integer> scores = new EnumMap<>(Category.class);
        
        // Initialiser les scores à 0
        for (Category category : Category.values()) {
            scores.put(category, 0);
        }

        // Calculer les scores
        for (Question question : questions) {
            Integer answer = answers.get(question.getId());
            if (answer != null && answer >= 1 && answer <= 5) {
                // Ajouter le score à la catégorie correspondante
                Category category = question.getCategory();
                scores.put(category, scores.get(category) + answer);
            }
        }

        // Convertir en pourcentages
        int totalQuestions = questions.size();
        if (totalQuestions > 0) {
            for (Category category : Category.values()) {
                int score = scores.get(category);
                int percentage = (score * 100) / (totalQuestions * 5); // 5 = max score par question
                scores.put(category, percentage);
            }
        }

        return scores;
    }
}
