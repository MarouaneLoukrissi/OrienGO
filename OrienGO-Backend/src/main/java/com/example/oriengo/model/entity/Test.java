package com.example.oriengo.model.entity;

import com.example.oriengo.model.enumeration.TestStatus;
import com.example.oriengo.model.enumeration.TestType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"student", "result", "questions"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tests",
        indexes = {
                @Index(name = "idx_tests_student", columnList = "student_id"),
                @Index(name = "idx_tests_status", columnList = "status")
        })
public class Test implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_seq")
    @SequenceGenerator(name = "test_seq", sequenceName = "test_seq", allocationSize = 50)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private Student student;


    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private TestType type;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TestStatus status = TestStatus.PENDING;

    @CreationTimestamp
    @Column(name = "started_at", updatable = false, nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at", nullable = true)
    private LocalDateTime completedAt;

    @Column(name = "duration_minutes", nullable = true)
    private Integer durationMinutes;

    @Column(name = "questions_count", nullable = true)
    private Integer questionsCount; //20 or 60

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TestQuestion> testQuestions = new HashSet<>();

    @OneToOne(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private TestResult result;

    @Builder.Default
    @Column(name = "soft_deleted", nullable = false)
    private boolean softDeleted = false;
}
