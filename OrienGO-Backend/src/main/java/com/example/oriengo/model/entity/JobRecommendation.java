package com.example.oriengo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"testResult", "job", "personalizedJobs"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_recommendations", indexes = {
        @Index(name = "idx_recommendation_result", columnList = "test_result_id"),
        @Index(name = "idx_recommendation_job", columnList = "job_id")
})
public class JobRecommendation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_recommendation_seq")
    @SequenceGenerator(name = "job_recommendation_seq", sequenceName = "job_recommendation_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_result_id", nullable = false)
    private TestResult testResult;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @OneToMany(mappedBy = "jobRecommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonalizedJob> personalizedJobs = new HashSet<>();

    @Column(name = "match_percentage", nullable = false)
    private Integer matchPercentage;

    @Column(name = "highlighted", nullable = false)
    private boolean highlighted = false; // To flag top-2 or top-3 jobs

}
