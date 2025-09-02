package com.example.oriengo.service;

import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobCreationException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobDeleteException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobGetException;
import com.example.oriengo.exception.custom.PersonalizedJob.PersonalizedJobUpdateException;
import com.example.oriengo.model.dto.PersonalizedJobRequestDto;
import com.example.oriengo.model.dto.PersonalizedJobResponseDto;
import com.example.oriengo.model.entity.*;
import com.example.oriengo.mapper.PersonalizedJobMapper;
import com.example.oriengo.model.enumeration.LinkType;
import com.example.oriengo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PersonalizedJobService {
    private final PersonalizedJobRepository repository;
    private final RestTemplate restTemplate;
    private final JobRecommendationRepository jobRecommendationRepository;
    private final PersonalizedJobMapper personalizedJobMapper;
    private final MessageSource messageSource; // Injected
    private final StudentRepository studentRepository;
    private final StudentPersonalizedJobLinkRepository studentPersonalizedJobLinkRepository;

    public List<PersonalizedJobResponseDto> findByLinkTypeAndStudent(LinkType type, Long studentId) {
        List<PersonalizedJob> jobs;

        if (studentId != null) {
            // Fetch jobs linked to the student with the given type
            jobs = repository.findByStudentIdAndLinkType(studentId, type);
        } else {
            // Fetch all jobs linked with the type (for coach/general)
            jobs = repository.findByLinkType(type);
        }

        // Map to DTOs, including student links if studentId is present
        if (studentId != null) {
            return personalizedJobMapper.toResponseDtoListWithLinks(jobs, studentId, studentPersonalizedJobLinkRepository);
        } else {
            return personalizedJobMapper.toResponseDtoList(jobs);
        }
    }

//    public List<LinkType> getLinkTypesForPersonalizedJobAndStudent(Long personalizedJobId, Long studentId) {
//        return studentPersonalizedJobLinkRepository.findByStudent_IdAndPersonalizedJob_Id(studentId, personalizedJobId)
//                .stream()
//                .map(StudentPersonalizedJobLink::getType)
//                .toList();
//    }

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
            PersonalizedJob personalizedJob = personalizedJobMapper.toEntity(requestDto);
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
            personalizedJobMapper.updateEntityFromDto(requestDto, existingPersonalizedJob);
            PersonalizedJob savedPersonalizedJob = repository.save(existingPersonalizedJob);
            log.info("Personalized job with ID {} successfully updated", savedPersonalizedJob.getId());
            return savedPersonalizedJob;
        } catch (Exception e) {
            log.error("Error updating personalized job with ID {}: {}", id, e.getMessage(), e);
            throw new PersonalizedJobUpdateException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.update.failed"));
        }
    }

    @Transactional
    public StudentPersonalizedJobLink linkJobToStudent(Long studentId, Long personalizedJobId, LinkType linkType) {
        Student student = studentRepository.findByIdAndIsDeletedFalse(studentId)
                .orElseThrow(() -> new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("student.not.found")));

        PersonalizedJob job = findById(personalizedJobId);

        // Check if already linked with same type
        Optional<StudentPersonalizedJobLink> existingLink = job.getStudentLinks().stream()
                .filter(link -> link.getStudent().getId().equals(studentId) && link.getType() == linkType)
                .findFirst();

        if (existingLink.isPresent()) {
            return existingLink.get(); // already linked
        }

        StudentPersonalizedJobLink link = StudentPersonalizedJobLink.builder()
                .student(student)
                .personalizedJob(job)
                .type(linkType)
                .build();

        job.getStudentLinks().add(link);
        repository.save(job); // cascade saves link

        return link;
    }

    @Transactional
    public void unlinkJobFromStudent(Long studentId, Long personalizedJobId, LinkType linkType) {
        // Fetch student
        Student student = studentRepository.findByIdAndIsDeletedFalse(studentId)
                .orElseThrow(() -> new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("student.not.found")));

        // Fetch job
        PersonalizedJob job = findById(personalizedJobId);

        // Find the link to remove
        Optional<StudentPersonalizedJobLink> linkToRemove = job.getStudentLinks().stream()
                .filter(link -> link.getStudent().getId().equals(studentId) && link.getType() == linkType)
                .findFirst();

        if (linkToRemove.isPresent()) {
            StudentPersonalizedJobLink link = linkToRemove.get();
            job.getStudentLinks().remove(link);  // remove from job
            repository.save(job);                 // cascade remove
        } else {
            throw new PersonalizedJobGetException(HttpStatus.NOT_FOUND, "Link not found for this student and type");
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

    public List<PersonalizedJobResponseDto> findByStudentId(Long studentId) {
        if (studentId == null) {
            log.warn("Attempted to fetch personalized jobs with null student ID");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("student.id.empty"));
        }

        log.info("Fetching personalized jobs for studentId: {}", studentId);

        List<PersonalizedJob> jobs = repository.findByStudentId(studentId);

        if (jobs.isEmpty()) {
            log.warn("No personalized jobs found for studentId: {}", studentId);
            throw new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.not.found"));
        }

        // Deduplicate before returning
        return personalizedJobMapper.toResponseDtoListWithLinks(removeDuplicatesExact(jobs), studentId, studentPersonalizedJobLinkRepository);
    }

    public List<PersonalizedJobResponseDto> findByJobRecommendationIds(List<Long> jobRecommendationIds, Long studentId) {
        if (jobRecommendationIds == null || jobRecommendationIds.isEmpty()) {
            log.warn("Attempted to fetch personalized jobs with null/empty jobRecommendationIds");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("personalizedJob.jobRecommendationIds.empty"));
        }

        log.info("Fetching personalized jobs for jobRecommendationIds: {}{}", jobRecommendationIds,
                (studentId != null ? " and studentId: " + studentId : ""));

        List<PersonalizedJob> jobs = repository.findByJobRecommendationIdIn(jobRecommendationIds);

        if (jobs.isEmpty()) {
            log.warn("No personalized jobs found for jobRecommendationIds: {}", jobRecommendationIds);
            throw new PersonalizedJobGetException(HttpStatus.NOT_FOUND, getMessage("personalizedJob.not.found"));
        }

        // Deduplicate before mapping
        List<PersonalizedJob> dedupedJobs = removeDuplicatesExact(jobs);

        // Map to DTOs
        if (studentId != null) {
            // Include student-specific links
            return personalizedJobMapper.toResponseDtoListWithLinks(dedupedJobs, studentId, studentPersonalizedJobLinkRepository);
        } else {
            // Just plain DTOs for coach or general view
            return personalizedJobMapper.toResponseDtoList(dedupedJobs);
        }
    }



    public java.util.Map<Long, java.util.List<PersonalizedJobResponseDto>> scrapeAndCreatePersonalizedJobs(
            java.util.List<Long> jobRecommendationIds, Long studentId) {

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

            String title = jobRecommendation.getJob().getTitle();
            java.util.List<PersonalizedJob> savedList = new java.util.ArrayList<>();

            try {
                String cleanTitle = title.replace("(", "")
                        .replace(")", "")
                        .replace("/", "-")
//                        .replace("%", "")
                        .trim();
                String encodedTitle = URLEncoder.encode(cleanTitle, StandardCharsets.UTF_8);
                String url = "http://localhost:5000/jobs/search?search_term=" + encodedTitle;
                ResponseEntity<java.util.Map[]> response = restTemplate.getForEntity(url, java.util.Map[].class);

                if (response.getBody() != null && response.getBody().length > 0) {
                    for (java.util.Map<String, Object> scrapedJob : response.getBody()) {
                        // Safely parse integers
                        Integer numEmployees = null;
                        try {
                            Object raw = scrapedJob.get("companyNumEmployees");
                            if (raw != null) {
                                numEmployees = Integer.parseInt(raw.toString().replaceAll("[+,]", "").trim());
                            }
                        } catch (NumberFormatException ignored) {
                            numEmployees = null;
                        }

                        PersonalizedJob pj = PersonalizedJob.builder()
                                .title(safeString(scrapedJob.get("title")))
                                .companyName(safeString(scrapedJob.get("companyName")))
                                .location(safeString(scrapedJob.get("location")))
                                .jobType(safeString(scrapedJob.get("jobType")))
                                .description(truncate(safeString(scrapedJob.get("description")), 1000))
                                .applyUrl(safeString(scrapedJob.get("applyUrl")))
                                .salaryRange(safeString(scrapedJob.get("salaryRange")))
                                .category("TECH")
                                .source(safeString(scrapedJob.get("source")))
                                .postedDate(parseDate(safeString(scrapedJob.get("postedDate"))))
                                .requiredSkills(safeString(scrapedJob.get("requiredSkills")))
                                .companyUrl(safeString(scrapedJob.get("companyUrl")))
                                .companyUrlDirect(safeString(scrapedJob.get("companyUrlDirect")))
                                .companyAddresses(safeString(scrapedJob.get("companyAddresses")))
                                .companyNumEmployees(numEmployees)
                                .companyRevenue(safeString(scrapedJob.get("companyRevenue")))
                                .companyDescription(safeString(scrapedJob.get("companyDescription")))
                                .experienceRange(safeString(scrapedJob.get("experienceRange")))
                                .emails(safeString(scrapedJob.get("emails")))
                                .companyIndustry(safeString(scrapedJob.get("companyIndustry")))
                                .jobUrlDirect(safeString(scrapedJob.get("jobUrlDirect")))
                                .isRemote(scrapedJob.get("isRemote") != null && Boolean.parseBoolean(scrapedJob.get("isRemote").toString()))
                                .expirationDate(java.time.LocalDate.now().plusMonths(1))
                                .duration("Permanent")
                                .matchPercentage(jobRecommendation.getMatchPercentage() != null ? jobRecommendation.getMatchPercentage() : 75)
                                .highlighted(false)
                                .softDeleted(false)
                                .build();

                        pj.setJobRecommendation(jobRecommendation);

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

                // fallback job
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

            result.put(recId, personalizedJobMapper.toResponseDtoList(savedList));
        }

        return result;
    }

    // Helper to safely convert nulls to empty strings
    private static String safeString(Object obj) {
        return obj != null ? obj.toString().trim() : null;
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
    // Add this inner static class for deduplication
    private static class DedupKey {
        private final String title;
        private final String applyUrl;

        public DedupKey(String title, String applyUrl) {
            this.title = title;
            this.applyUrl = applyUrl;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DedupKey)) return false;
            DedupKey key = (DedupKey) o;
            return Objects.equals(title, key.title) &&
                    Objects.equals(applyUrl, key.applyUrl);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, applyUrl);
        }
    }

    // Unified deduplication method
    private List<PersonalizedJob> removeDuplicatesExact(List<PersonalizedJob> jobs) {
        Map<DedupKey, PersonalizedJob> map = new LinkedHashMap<>();
        for (PersonalizedJob job : jobs) {
            DedupKey key = new DedupKey(job.getTitle(), job.getApplyUrl());
            if (!map.containsKey(key) || job.getMatchPercentage() > map.get(key).getMatchPercentage()) {
                map.put(key, job);
            }
        }
        return new ArrayList<>(map.values());
    }
} 