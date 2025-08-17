package com.example.oriengo.model.entity;

import com.example.oriengo.model.enumeration.LinkType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"student", "personalizedJob"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student_personalized_job_links", indexes = {
        @Index(name = "idx_student_personalized_job_type", columnList = "student_id, personalized_job_id, type")
})
public class StudentPersonalizedJobLink implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_personalized_job_link_seq")
    @SequenceGenerator(name = "student_personalized_job_link_seq", sequenceName = "student_personalized_job_link_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "personalized_job_id", nullable = false)
    private PersonalizedJob personalizedJob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LinkType type;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
