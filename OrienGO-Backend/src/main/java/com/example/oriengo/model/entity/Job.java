package com.example.oriengo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"trainings", "studentLinks"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jobs", indexes = {
        @Index(name = "idx_job_category", columnList = "category")
})
public class Job implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_seq")
    @SequenceGenerator(name = "job_seq", sequenceName = "job_seq", allocationSize = 50)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private JobCategory category;

    @Column(length = 100)
    private String education; // e.g. Master's in Psychology

    @Column(name = "salary_range", length = 100)
    private String salaryRange; // e.g. "30,000 - 50,000 USD/year"

    @Column(name = "job_market", length = 100)
    private String jobMarket; // e.g. "High demand", "Stable"

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "job_tags", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "tag", length = 50)
    private Set<String> tags = new HashSet<>(); // e.g. ["Listen", "Teamwork"]

    @Builder.Default
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<JobRecommendation> jobRecommendations = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "job_training_links",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id")
    )
    private Set<Training> trainings = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<StudentJobLink> studentLinks = new HashSet<>();

    @Column(name = "soft_deleted", nullable = false)
    private boolean softDeleted = false;

    @Version
    private Long version;

}
