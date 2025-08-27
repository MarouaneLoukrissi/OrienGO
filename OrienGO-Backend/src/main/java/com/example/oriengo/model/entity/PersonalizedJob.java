package com.example.oriengo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "personalized_jobs", indexes = {
        @Index(name = "idx_job_expiry", columnList = "expiration_date"),
        @Index(name = "idx_personalized_job_category", columnList = "category")
})
public class PersonalizedJob implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personalized_job_seq")
    @SequenceGenerator(name = "personalized_job_seq", sequenceName = "personalized_job_seq", allocationSize = 50)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 255)
    private String companyName;

    @Column(length = 255)
    private String location;

    @Column(name = "job_type", length = 50)
    private String jobType;

    @Column(length = 1000)
    private String description;

    @Column(name = "apply_url", length = 1000)
    private String applyUrl;

    @Column(name = "salary_range", length = 100)
    private String salaryRange;

    @Column(length = 100)
    private String category;

    @Column(length = 100)
    private String source;

    @Column(name = "posted_date")
    private LocalDate postedDate;

    //------------------------------------------------------------------//

    @Column(name = "required_skills", length = 2000)  // can store comma-separated skills
    private String requiredSkills;

    @Column(name = "company_url", length = 1000)
    private String companyUrl;

    @Column(name = "company_url_direct", length = 1000)
    private String companyUrlDirect;

    @Column(name = "company_address", length = 1000)
    private String companyAddresses; // can store comma-separated company addresses

    @Column(name = "company_num_employees")
    private Integer companyNumEmployees;

    @Column(name = "company_revenue", length = 255)
    private String companyRevenue;

    @Column(name = "company_description", columnDefinition = "TEXT")
    private String companyDescription;

    @Column(name = "experience_range", length = 255)
    private String experienceRange;

    @Column(name = "emails", length = 2000)  // can store comma-separated emails
    private String emails;

    @Column(name = "company_industry", length = 255)
    private String companyIndustry;

    @Column(name = "job_url_direct", length = 1000)
    private String jobUrlDirect;

    @Column(name = "is_remote")
    private Boolean isRemote;



    //------------------------------------------------------------------//



    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "duration", length = 100)
    private String duration;

    @ElementCollection
    @CollectionTable(name = "job_advantages", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "advantage")
    private Set<String> advantages = new HashSet<>();

    @Column(name = "match_percentage", nullable = false)
    private Integer matchPercentage;

    @Column(name = "highlighted", nullable = false)
    private boolean highlighted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_recommendation_id")
    private JobRecommendation jobRecommendation;

    @Builder.Default
    @OneToMany(mappedBy = "personalizedJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<StudentPersonalizedJobLink> studentLinks = new HashSet<>();

    @Column(name = "soft_deleted", nullable = false)
    private boolean softDeleted = false;

}
