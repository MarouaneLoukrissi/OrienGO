package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobCreationException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobDeleteException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobGetException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobUpdateException;
import com.example.oriengo.model.dto.PersonalizedJobRequestDto;
import com.example.oriengo.model.entity.PersonalizedJob;
import com.example.oriengo.model.entity.JobRecommendation;
import com.example.oriengo.mapper.PersonalizedJobMapper;
import com.example.oriengo.repository.StudentRepository;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.model.enumeration.LinkType;
import com.example.oriengo.model.entity.StudentPersonalizedJobLink;
import com.example.oriengo.model.dto.PersonalizedJobResponseDto;
import com.example.oriengo.repository.PersonalizedJobRepository;
import com.example.oriengo.repository.JobRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PersonalizedJobService {
    private final PersonalizedJobRepository repository;
    private final JobRecommendationRepository jobRecommendationRepository;
    private final StudentRepository studentRepository;

    private final PersonalizedJobMapper mapper;
    
    @Autowired
    private RestTemplate restTemplate;
    private final MessageSource messageSource; // Injected

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public List<PersonalizedJob> findAll() {
        try {
            log.info("Fetching personalized job");
            List<PersonalizedJob> personalizedJobs = repository.findAll();
            log.info("Found {} personalized job", personalizedJobs.size());
            return personalizedJobs;
        } catch (Exception e) {
            log.error("Failed to fetch personalized job : {}", e.getMessage(), e);
            throw new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.not.found"));
        }
    }

    public PersonalizedJob findById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch personalized job with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.id.empty"));
        }

        log.info("Fetching personalized job with ID: {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Personalized job not found with ID: {}", id);
                    return new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.not.found"));
                });
    }

    public PersonalizedJob create(PersonalizedJobRequestDto requestDto) {
        if (requestDto == null) {
            log.warn("personalized job request cannot be null");
            throw new PersonalizedJobCreationException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.dto.empty"));
        }
        try {
            JobRecommendation jobRecommendation = null;
            if (requestDto.getJobRecommendationId() != null) {
                jobRecommendation = jobRecommendationRepository.findById(requestDto.getJobRecommendationId())
                        .orElseThrow(() -> new PersonalizedJobCreationException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.jobRecommendation.empty")));
            }
            PersonalizedJob personalizedJob = mapper.toEntity(requestDto);
            if (jobRecommendation != null) {
                personalizedJob.setJobRecommendation(jobRecommendation);
            }

            return repository.save(personalizedJob);
        } catch (Exception e) {
            log.error("Unexpected error during personalized job creation : {}", e.getMessage(), e);
            throw new PersonalizedJobCreationException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.create.failed"));
        }
    }

    public PersonalizedJob update(Long id, PersonalizedJobRequestDto requestDto) {
        if (id == null) {
            log.warn("Attempted to update personalized job with null ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.id.empty"));
        } else if (requestDto == null) {
            log.warn("personalized job request cannot be null");
            throw new PersonalizedJobUpdateException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.dto.empty"));
        }
        try{
            PersonalizedJob existingPersonalizedJob = findById(id);

            if (requestDto.getJobRecommendationId() != null) {
                JobRecommendation jobRecommendation = jobRecommendationRepository.findById(requestDto.getJobRecommendationId())
                        .orElseThrow(() -> new PersonalizedJobUpdateException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.jobRecommendation.not.found")));
                existingPersonalizedJob.setJobRecommendation(jobRecommendation);
            }

            mapper.updateEntityFromDto(requestDto, existingPersonalizedJob);
            PersonalizedJob savedPersonalizedJob = repository.save(existingPersonalizedJob);
            log.info("Personalized job with ID {} successfully updated", savedPersonalizedJob.getId());
            return savedPersonalizedJob;
        } catch (Exception e) {
            log.error("Error updating personalized job with ID {}: {}", id, e.getMessage(), e);
            throw new PersonalizedJobUpdateException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.update.failed"));
        }
    }

    public void deleteById(Long id) {
        if (id == null) {
            log.warn("Attempted delete with null personalized job ID");
            throw new PersonalizedJobDeleteException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.id.empty"));
        }
        try {
            log.info("Attempting delete for personalized job with ID: {}", id);
            PersonalizedJob personalizedJob = findById(id);
            repository.deleteById(personalizedJob.getId());
            log.info("Successfully deleted personalized job with ID: {}", personalizedJob.getId());
        } catch (Exception e) {
            log.error("Error during delete of personalized job with ID {}: {}", id, e.getMessage(), e);
            throw new PersonalizedJobDeleteException(HttpStatus.CONFLICT, getMessage("personalized.delete.failed"));
        }
    }

    public List<PersonalizedJob> findByJobRecommendationId(Long jobRecommendationId) {
        if (jobRecommendationId == null) {
            log.warn("Attempted to fetch personalized job with null job recommendation ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.id.empty"));
        }

        log.info("Fetching personalized job with job recommendation ID: {}", jobRecommendationId);

        return repository.findByJobRecommendationId(jobRecommendationId)
                .orElseThrow(() -> {
                    log.error("Personalized job not found with job recommendation ID: {}", jobRecommendationId);
                    return new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.not.found"));
                });
    }

    public List<PersonalizedJob> findHighlightedJobs() {
        try {
            log.info("Fetching highlighted job");
            List<PersonalizedJob> personalizedJobs = repository.findByHighlightedTrue();
            log.info("Found {} highlighted job", personalizedJobs.size());
            return personalizedJobs;
        } catch (Exception e) {
            log.error("Failed to fetch highlighted jobs : {}", e.getMessage(), e);
            throw new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.not.found"));
        }
    }

    public java.util.Map<Long, java.util.List<PersonalizedJobResponseDto>> scrapeAndCreatePersonalizedJobs(java.util.List<Long> jobRecommendationIds, Long studentId) {
        if (jobRecommendationIds == null || jobRecommendationIds.isEmpty()) {
            throw new PersonalizedJobCreationException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.dto.empty"));
        }
        if (studentId == null) {
            throw new PersonalizedJobCreationException(HttpStatus.BAD_REQUEST, getMessage("student.id.empty"));
        }

        Student student = studentRepository.findByIdAndIsDeletedFalse(studentId)
                .orElseThrow(() -> new PersonalizedJobCreationException(HttpStatus.NOT_FOUND, getMessage("student.not.found")));

        java.util.Map<Long, java.util.List<PersonalizedJobResponseDto>> result = new java.util.HashMap<>();

        for (Long recId : jobRecommendationIds) {
            JobRecommendation jobRecommendation = jobRecommendationRepository.findById(recId)
                    .orElseThrow(() -> new PersonalizedJobCreationException(HttpStatus.NOT_FOUND, getMessage("jobRecommendation.not.found")));

            // Get job title for scraping
            String title = jobRecommendation.getJob().getTitle();
            
            // Call Flask API for job scraping
            java.util.List<PersonalizedJob> savedList = new java.util.ArrayList<>();
            try {
                String url = "http://localhost:5000/jobs/search?search_term=" + java.net.URLEncoder.encode(title, "UTF-8");
                ResponseEntity<java.util.Map[]> response = restTemplate.getForEntity(url, java.util.Map[].class);
                
                if (response.getBody() != null && response.getBody().length > 0) {
                    for (java.util.Map<String, Object> scrapedJob : response.getBody()) {
                        PersonalizedJob pj = PersonalizedJob.builder()
                                .title((String) scrapedJob.get("title"))
                                .companyName((String) scrapedJob.get("companyName"))
                                .location((String) scrapedJob.get("location"))
                                .jobType((String) scrapedJob.get("jobType"))
                                .description(truncate((String) scrapedJob.get("description"), 1000))
                                .applyUrl((String) scrapedJob.get("applyUrl"))
                                .salaryRange((String) scrapedJob.get("salaryRange"))
                                .category("TECH") // Default category
                                .source((String) scrapedJob.get("source"))
                                .postedDate(parseDate((String) scrapedJob.get("postedDate")))
                                .requiredSkills((String) scrapedJob.get("requiredSkills"))
                                .companyUrl((String) scrapedJob.get("companyUrl"))
                                .companyUrlDirect((String) scrapedJob.get("companyUrlDirect"))
                                .companyAddresses((String) scrapedJob.get("companyAddresses"))
                                .companyNumEmployees(scrapedJob.get("companyNumEmployees") != null ? 
                                    Integer.valueOf(scrapedJob.get("companyNumEmployees").toString()) : null)
                                .companyRevenue((String) scrapedJob.get("companyRevenue"))
                                .companyDescription((String) scrapedJob.get("companyDescription"))
                                .experienceRange((String) scrapedJob.get("experienceRange"))
                                .emails((String) scrapedJob.get("emails"))
                                .companyIndustry((String) scrapedJob.get("companyIndustry"))
                                .jobUrlDirect((String) scrapedJob.get("jobUrlDirect"))
                                .isRemote(scrapedJob.get("isRemote") != null ? 
                                    Boolean.valueOf(scrapedJob.get("isRemote").toString()) : false)
                                .expirationDate(java.time.LocalDate.now().plusMonths(1))
                                .duration("Permanent")
                                .matchPercentage(jobRecommendation.getMatchPercentage() != null ? 
                                    jobRecommendation.getMatchPercentage() : 75)
                                .highlighted(false)
                                .softDeleted(false)
                                .build();

                        pj.setJobRecommendation(jobRecommendation);

                        // Create student link with LinkType.NORMAL
                        StudentPersonalizedJobLink link = StudentPersonalizedJobLink.builder()
                                .student(student)
                                .personalizedJob(pj)
                                .type(LinkType.NORMAL)
                                .build();
                        pj.getStudentLinks().add(link);

                        PersonalizedJob saved = repository.save(pj);
                        savedList.add(saved);
                    }
                } else {
                    log.warn("No jobs found for title: {}", title);
                }
            } catch (Exception e) {
                log.error("Scraping failed for title: {}", title, e);
                // Create a fallback job if scraping fails
                PersonalizedJob fallbackJob = createFallbackJob(jobRecommendation, recId);
                fallbackJob.setJobRecommendation(jobRecommendation);
                
                StudentPersonalizedJobLink link = StudentPersonalizedJobLink.builder()
                        .student(student)
                        .personalizedJob(fallbackJob)
                        .type(LinkType.NORMAL)
                        .build();
                fallbackJob.getStudentLinks().add(link);
                
                PersonalizedJob saved = repository.save(fallbackJob);
                savedList.add(saved);
            }
            
            result.put(recId, mapper.toResponseDtoList(savedList));
        }

        return result;
    }

    private static String truncate(String value, int max) {
        if (value == null) return null;
        return value.length() <= max ? value : value.substring(0, max);
    }

    private static java.time.LocalDate parseDate(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return java.time.LocalDate.parse(value.substring(0, 10));
        } catch (Exception e) {
            return null;
        }
    }

    private PersonalizedJob createFallbackJob(JobRecommendation jobRecommendation, Long recId) {
        return PersonalizedJob.builder()
                .title("Fallback Job for " + jobRecommendation.getJob().getTitle())
                .companyName("Fallback Company")
                .location("Fallback Location")
                .jobType("FULL_TIME")
                .description("Fallback job created due to scraping failure for recommendation ID: " + recId)
                .applyUrl("https://example.com/apply")
                .salaryRange("30000 - 50000")
                .category("TECH")
                .source("FALLBACK")
                .postedDate(java.time.LocalDate.now())
                .requiredSkills("Java, Spring Boot, PostgreSQL")
                .companyUrl("https://example.com")
                .companyUrlDirect("https://example.com")
                .companyAddresses("Fallback Address")
                .companyNumEmployees(100)
                .companyRevenue("$1M - $10M")
                .companyDescription("Fallback company description")
                .experienceRange("2-5 years")
                .emails("hr@example.com")
                .companyIndustry("Technology")
                .jobUrlDirect("https://example.com/job")
                .isRemote(false)
                .expirationDate(java.time.LocalDate.now().plusMonths(1))
                .duration("Permanent")
                .matchPercentage(jobRecommendation.getMatchPercentage() != null ? jobRecommendation.getMatchPercentage() : 75)
                .highlighted(false)
                .softDeleted(false)
                .build();
    }
} 