package com.example.oriengo.model.entity;

import com.example.oriengo.model.enumeration.ConnectionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coach_student_connections",
        uniqueConstraints = @UniqueConstraint(columnNames = {"coach_id", "student_id"}))
public class CoachStudentConnection {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conn_seq")
    @SequenceGenerator(name = "conn_seq", sequenceName = "conn_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConnectionStatus status = ConnectionStatus.PENDING;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

}