package com.example.oriengo.model.entity;

import com.example.oriengo.model.enumeration.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "test")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "test_results", indexes = {
        @Index(name = "idx_result_test", columnList = "test_id"),
        @Index(name = "idx_result_dominant", columnList = "dominant_type")
})
public class TestResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_result_seq")
    @SequenceGenerator(name = "test_result_seq", sequenceName = "test_result_seq", allocationSize = 50)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_id", nullable = false, unique = true)
    @JsonIgnore
    private Test test;

    @Enumerated(EnumType.STRING)
    @Column(name = "dominant_type", nullable = false, length = 20)
    private Category dominantType; // Most dominant profile (e.g., REALISTIC)

    @Column(name = "dominant_type_description", length = 20)
    private String dominantTypeDescription; // e.g., "You are a Realistic type, practical and hands-on..."

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "test_result_scores", joinColumns = @JoinColumn(name = "result_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "type", length = 20)
    @Column(name = "percentage", nullable = false)
    private Map<Category, Integer> scores = new EnumMap<>(Category.class); //scores.put(Category.REALISTIC, 44);

    @Column(name = "key_points", length = 4000)
    private String keyPoints; // e.g., "key points of your profile ..."

    @OneToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "pdf_id", referencedColumnName = "id")
    @JsonIgnore
    private Media pdf; // Link to generated PDF media

    @Column(name = "shared", nullable = false)
    private boolean shared = false; // If user shared the result externally

    @Column(name = "downloaded", nullable = false)
    private boolean downloaded = false; // If user downloaded PDF

    @Column(name = "soft_deleted", nullable = false)
    private boolean softDeleted = false;

    @OneToMany(mappedBy = "testResult", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<JobRecommendation> jobRecommendations = new HashSet<>();

    @OneToMany(mappedBy = "testResult", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<TrainingRecommendation> trainingRecommendations = new HashSet<>();
}