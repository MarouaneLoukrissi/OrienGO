package com.example.oriengo.service;

import com.example.oriengo.model.dto.RecommendationRequestDTO;
import com.example.oriengo.model.dto.RecommendationResponseDTO;
import com.example.oriengo.model.dto.JobDTO;
import com.example.oriengo.model.dto.TrainingDTO;
import com.example.oriengo.model.entity.*;
import com.example.oriengo.model.enumeration.*;
import com.example.oriengo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final TestResultRepository testResultRepository;
    private final JobRepository jobRepository;
    private final TrainingRepository trainingRepository;
    private final JobRecommendationRepository jobRecommendationRepository;
    private final TrainingRecommendationRepository trainingRecommendationRepository;
    private final StudentJobLinkRepository studentJobLinkRepository;
    private final StudentTrainingLinkRepository studentTrainingLinkRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public RecommendationResponseDTO processRecommendations(Long studentId, Long testResultId) {
        log.info("Processing recommendations for studentId: {} and testResultId: {}", studentId, testResultId);
//
//        // 1. Récupérer Student et TestResult
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        Student student = studentService.getStudentById(studentId, false);


        TestResult testResult = testResultRepository.findById(testResultId)
                .orElseThrow(() -> new RuntimeException("TestResult not found with id: " + testResultId));

        // 2. Extraire les scores RIASEC
        RecommendationRequestDTO request = extractRiasecScores(testResult);
        log.info("Extracted RIASEC scores: {}", request);

        // 3. Appeler l'API Flask
        RecommendationResponseDTO flaskResponse = callFlaskAPI(request);
        log.info("Received response from Flask API: {} jobs, {} trainings", 
                flaskResponse.getJobs().size(), flaskResponse.getTrainings().size());

        // 4. Traiter les recommandations de jobs
        processJobRecommendations(flaskResponse.getJobs(), testResult, student);

        // 5. Traiter les recommandations de trainings
        processTrainingRecommendations(flaskResponse.getTrainings(), testResult, student);

        // 6. Lier les trainings aux jobs (optionnel)
        linkTrainingsToJobs(flaskResponse.getTrainings(), flaskResponse.getJobs());

        log.info("Successfully processed all recommendations");
        return flaskResponse;
    }

    private RecommendationRequestDTO extractRiasecScores(TestResult testResult) {
        RecommendationRequestDTO.RiasecScores riasecScores = RecommendationRequestDTO.RiasecScores.builder()
                .realistic(testResult.getScores().get(Category.REALISTIC))
                .investigative(testResult.getScores().get(Category.INVESTIGATIVE))
                .artistic(testResult.getScores().get(Category.ARTISTIC))
                .social(testResult.getScores().get(Category.SOCIAL))
                .enterprising(testResult.getScores().get(Category.ENTERPRISING))
                .conventional(testResult.getScores().get(Category.CONVENTIONAL))
                .build();

        return RecommendationRequestDTO.builder()
                .riasec(riasecScores)
                .build();
    }

    private RecommendationResponseDTO callFlaskAPI(RecommendationRequestDTO request) {
        try {
            String flaskUrl = "http://127.0.0.1:5000/recommendations";
            log.info("Calling Flask API at: {}", flaskUrl);
            
            RecommendationResponseDTO response = restTemplate.postForObject(flaskUrl, request, RecommendationResponseDTO.class);
            
            if (response == null) {
                throw new RuntimeException("No response received from Flask API");
            }
            
            return response;
        } catch (Exception e) {
            log.error("Error calling Flask API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call Flask API: " + e.getMessage(), e);
        }
    }

    private void processJobRecommendations(List<JobDTO> jobDTOs, TestResult testResult, Student student) {
        for (JobDTO jobDTO : jobDTOs) {
            // Créer ou mettre à jour le Job
            Job job = createOrUpdateJob(jobDTO);
            
            // Créer JobRecommendation
            JobRecommendation jobRecommendation = JobRecommendation.builder()
                    .job(job)
                    .testResult(testResult)
                    .matchPercentage(15) // Valeur par défaut temporaire a modifier apres
                    .build();
            jobRecommendationRepository.save(jobRecommendation);
            
            // Créer StudentJobLink
            StudentJobLink studentJobLink = StudentJobLink.builder()
                    .student(student)
                    .job(job)
                    .type(LinkType.SAVED)
                    .build();
            studentJobLinkRepository.save(studentJobLink);
            
            log.info("Created job recommendation: {} for student: {}", job.getTitle(), student.getId());
        }
    }

    private void processTrainingRecommendations(List<TrainingDTO> trainingDTOs, TestResult testResult, Student student) {
        for (TrainingDTO trainingDTO : trainingDTOs) {
            // Créer ou mettre à jour le Training
            Training training = createOrUpdateTraining(trainingDTO);
            
            // Créer TrainingRecommendation
            TrainingRecommendation trainingRecommendation = TrainingRecommendation.builder()
                    .training(training)
                    .testResult(testResult)
                    .build();
            trainingRecommendationRepository.save(trainingRecommendation);
            
            // Créer StudentTrainingLink
            StudentTrainingLink studentTrainingLink = StudentTrainingLink.builder()
                    .student(student)
                    .training(training)
                    .type(LinkType.SAVED)
                    .build();
            studentTrainingLinkRepository.save(studentTrainingLink);
            
            log.info("Created training recommendation: {} for student: {}", training.getName(), student.getId());
        }
    }

    private Job createOrUpdateJob(JobDTO jobDTO) {
        // Vérifier si le job existe déjà par titre
        Optional<Job> existingJob = jobRepository.findAll().stream()
                .filter(job -> job.getTitle().equalsIgnoreCase(jobDTO.getTitle()))
                .findFirst();

        if (existingJob.isPresent()) {
            log.info("Job already exists: {}", jobDTO.getTitle());
            return existingJob.get();
        }

        // Créer un nouveau job
        Job job = Job.builder()
                .title(jobDTO.getTitle())
                .description(jobDTO.getDescription())
                .category(jobDTO.getCategory()) // déjà un enum dans JobDTO → plus besoin de parse
                .jobMarket(jobDTO.getJobMarket())
                .education(jobDTO.getEducation())
                .salaryRange(jobDTO.getSalaryRange())
                .tags(jobDTO.getTags() != null ? jobDTO.getTags() : new HashSet<>())
                .riasecArtistic(jobDTO.getRiasecArtistic() != null ? jobDTO.getRiasecArtistic() : 0.0)
                .riasecConventional(jobDTO.getRiasecConventional() != null ? jobDTO.getRiasecConventional() : 0.0)
                .riasecEnterprising(jobDTO.getRiasecEnterprising() != null ? jobDTO.getRiasecEnterprising() : 0.0)
                .riasecInvestigative(jobDTO.getRiasecInvestigative() != null ? jobDTO.getRiasecInvestigative() : 0.0)
                .riasecRealistic(jobDTO.getRiasecRealistic() != null ? jobDTO.getRiasecRealistic() : 0.0)
                .riasecSocial(jobDTO.getRiasecSocial() != null ? jobDTO.getRiasecSocial() : 0.0)
                .softDeleted(false)
                .active(true) // par défaut
                .build();


        Job savedJob = jobRepository.save(job);
        log.info("Created new job: {}", savedJob.getTitle());
        return savedJob;
    }

    private Training createOrUpdateTraining(TrainingDTO trainingDTO) {
        // Vérifier si le training existe déjà par nom
        Optional<Training> existingTraining = trainingRepository.findAll().stream()
                .filter(training -> training.getName().equalsIgnoreCase(trainingDTO.getName()))
                .findFirst();

        if (existingTraining.isPresent()) {
            log.info("Training already exists: {}", trainingDTO.getName());
            return existingTraining.get();
        }

        // Créer un nouveau training
        Training training = Training.builder()
                .name(trainingDTO.getName())
                .type(parseTrainingType(trainingDTO.getType()))
                .description(trainingDTO.getDescription())
                .duration(trainingDTO.getDuration())
                .specializations(trainingDTO.getSpecializations()) // Training utilise List<String>
                .softDeleted(false)
                .build();

        Training savedTraining = trainingRepository.save(training);
        log.info("Created new training: {}", savedTraining.getName());
        return savedTraining;
    }

    private void linkTrainingsToJobs(List<TrainingDTO> trainingDTOs, List<JobDTO> jobDTOs) {
        // Cette méthode peut être implémentée pour lier les trainings aux jobs
        // Pour l'instant, c'est optionnel comme spécifié dans le ticket
        log.info("Linking trainings to jobs (optional feature)");
    }

    private Set<String> convertListToSet(List<String> list) {
        if (list == null) {
            return Set.of();
        }
        return list.stream().collect(Collectors.toSet());
    }

    private JobCategory parseJobCategory(String category) {
        try {
            return JobCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Unknown job category: {}, using TECH as default", category);
            return JobCategory.TECH;
        }
    }

    private TrainingType parseTrainingType(String type) {
        try {
            return TrainingType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Unknown training type: {}, using ONLINE_COURSE as default", type);
            return TrainingType.ONLINE_COURSE;
        }
    }
}
