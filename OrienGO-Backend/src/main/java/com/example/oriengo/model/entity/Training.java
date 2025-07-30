package com.example.oriengo.model.entity;

import com.example.oriengo.model.enumeration.TrainingType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"jobs","studentLinks"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "trainings", indexes = {
        @Index(name = "idx_training_type", columnList = "type")
})
public class Training implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_seq")
    @SequenceGenerator(name = "training_seq", sequenceName = "training_seq", allocationSize = 50)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name; // e.g., "Engineering School"

    @Column(length = 1000)
    private String description;

    @Column(name = "duration", length = 50)
    private String duration; // e.g., "3–5 years"

    @ElementCollection
    @CollectionTable(name = "training_specializations", joinColumns = @JoinColumn(name = "training_id"))
    @Column(name = "specialization", length = 100)
    private List<String> specializations; // e.g., "Psychology", "Teaching"

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private TrainingType type; // e.g., UNIVERSITY, BOOTCAMP, VOCATIONAL

    @Column(name = "highlighted", nullable = false)
    private boolean highlighted = false;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TrainingRecommendation> trainingRecommendations = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "trainings")
    private Set<Job> jobs = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<StudentTrainingLink> studentLinks = new HashSet<>();

    @Column(name = "soft_deleted", nullable = false)
    private boolean softDeleted = false;

    @Version
    private Long version;

}
