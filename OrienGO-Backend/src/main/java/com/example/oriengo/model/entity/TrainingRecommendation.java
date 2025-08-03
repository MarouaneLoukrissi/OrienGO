package com.example.oriengo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training_recommendations")
public class TrainingRecommendation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_rec_seq")
    @SequenceGenerator(name = "training_rec_seq", sequenceName = "training_rec_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_result_id", nullable = false)
    private TestResult testResult;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @Column(name = "match_percentage", nullable = false)
    private Integer matchPercentage;

    @Column(name = "highlighted", nullable = false)
    private boolean highlighted = false;

}
