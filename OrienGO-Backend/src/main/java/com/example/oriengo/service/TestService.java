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
import com.example.oriengo.model.enumeration.GenderType;

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
        // 1. Créer le Student manuellement pour s'assurer que tous les champs obligatoires sont définis
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setEmail(studentDto.getEmail());
        student.setPhoneNumber(studentDto.getPhoneNumber());
        
        // Gérer l'âge - convertir String en int ou utiliser 0 par défaut
        try {
            student.setAge(studentDto.getAge() != null ? Integer.parseInt(studentDto.getAge()) : 0);
        } catch (NumberFormatException e) {
            student.setAge(0);
        }
        
        // Gérer le genre - convertir String en GenderType ou utiliser null
        if (studentDto.getGender() != null && !studentDto.getGender().trim().isEmpty()) {
            try {
                student.setGender(GenderType.valueOf(studentDto.getGender().toUpperCase()));
            } catch (IllegalArgumentException e) {
                student.setGender(null);
            }
        }
        
        student.setSchool(studentDto.getSchool());
        student.setFieldOfStudy(studentDto.getFieldOfStudy());
        student.setEducationLevel(studentDto.getEducationLevel());
        student.setLocation(studentDto.getLocation());
        student.setProfileVisibility(studentDto.getProfileVisibility());
        student.setAccountPrivacy(studentDto.getAccountPrivacy());
        student.setMessagePermission(studentDto.getMessagePermission());
        student.setEnabled(true); // Activer le compte par défaut
        student.setPassword("defaultPassword"); // Mot de passe temporaire
        
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
    public TestResult submitTestAndCalculateResults(Long testId, Map<Long, Integer> answers) {
        //  Récupérer le test avec ses questions
        Test test = testRepository.findByIdWithRelations(testId)
                .orElseThrow(() -> new RuntimeException("Test not found with id " + testId));

        //  Vérifier que toutes les questions sont répondues
        validateTestCompletion(test.getQuestions(), answers);

        //  Calculer les scores par catégorie RIASEC
        Map<Category, Integer> scores = calculateRIASECScores(test.getQuestions(), answers);

        //  Déterminer le type dominant (gérer les égalités)
        Category dominantType = determineDominantType(scores);

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

    private void validateTestCompletion(Set<Question> questions, Map<Long, Integer> answers) {
        // Vérifier que toutes les questions ont une réponse
        for (Question question : questions) {
            Integer answer = answers.get(question.getId());
            if (answer == null || answer < 1 || answer > 5) {
                throw new RuntimeException("Test incomplet : Question " + question.getId() + 
                    " n'a pas de réponse valide. Toutes les questions doivent être répondues avec une valeur entre 1 et 5.");
            }
        }
        
        // Vérifier qu'il n'y a pas de réponses en trop
        if (answers.size() > questions.size()) {
            throw new RuntimeException("Trop de réponses fournies. Le test contient " + 
                questions.size() + " questions mais " + answers.size() + " réponses ont été fournies.");
        }
    }

    private Map<Category, Integer> calculateRIASECScores(Set<Question> questions, Map<Long, Integer> answers) {
        Map<Category, Integer> scores = new EnumMap<>(Category.class);
        Map<Category, Integer> questionCounts = new EnumMap<>(Category.class);
        
        // Initialiser les scores et compteurs à 0
        for (Category category : Category.values()) {
            scores.put(category, 0);
            questionCounts.put(category, 0);
        }

        // Compter les questions par catégorie et calculer les scores
        for (Question question : questions) {
            Category category = question.getCategory();
            questionCounts.put(category, questionCounts.get(category) + 1);
            
            Integer answer = answers.get(question.getId());
            if (answer != null && answer >= 1 && answer <= 5) {
                scores.put(category, scores.get(category) + answer);
            }
        }

        // Convertir en pourcentages (basé sur toutes les questions de la catégorie)
        for (Category category : Category.values()) {
            int score = scores.get(category);
            int questionCount = questionCounts.get(category);
            
            if (questionCount > 0) {
                // Calculer le pourcentage : (score actuel / score max possible) * 100
                int maxPossibleScore = questionCount * 5; // 5 = max score par question
                int percentage = (score * 100) / maxPossibleScore;
                scores.put(category, percentage);
            } else {
                scores.put(category, 0);
            }
        }

        return scores;
    }

    private Category determineDominantType(Map<Category, Integer> scores) {
        // Trouver le score maximum
        int maxScore = scores.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        
        // Trouver toutes les catégories avec le score maximum
        List<Category> dominantCategories = scores.entrySet().stream()
                .filter(entry -> entry.getValue().equals(maxScore))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        // Si une seule catégorie dominante, la retourner
        if (dominantCategories.size() == 1) {
            return dominantCategories.get(0);
        }
        
        // En cas d'égalité, utiliser une logique de priorité basée sur l'ordre RIASEC
        // Ordre de priorité : REALISTIC > INVESTIGATIVE > ARTISTIC > SOCIAL > ENTERPRISING > CONVENTIONAL
        Category[] priorityOrder = {
            Category.REALISTIC,
            Category.INVESTIGATIVE, 
            Category.ARTISTIC,
            Category.SOCIAL,
            Category.ENTERPRISING,
            Category.CONVENTIONAL
        };
        
        for (Category priorityCategory : priorityOrder) {
            if (dominantCategories.contains(priorityCategory)) {
                return priorityCategory;
            }
        }
        
        // Fallback
        return Category.REALISTIC;
    }
}
