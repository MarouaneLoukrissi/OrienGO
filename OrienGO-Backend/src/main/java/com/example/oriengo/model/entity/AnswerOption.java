package com.example.oriengo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "question")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "answer_options",
        indexes = {
                @Index(name = "idx_answer_option_question", columnList = "question_id")
        }
)
public class AnswerOption implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_option_seq")
    @SequenceGenerator(name = "answer_option_seq", sequenceName = "answer_option_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private Integer optionIndex;  // 0,1,2,3...

    @Column(nullable = false, length = 500)
    private String text;  // The answer text shown to users

    @OneToMany(mappedBy = "chosenAnswer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TestQuestion> testQuestions = new HashSet<>();
}
