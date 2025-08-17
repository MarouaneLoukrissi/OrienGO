package com.example.oriengo.model.entity;

import com.example.oriengo.model.enumeration.MediaType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "medias",
    indexes = {
        @Index(name = "idx_media_user_type", columnList = "user_id, type")
    }
)
public class Media implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medias_seq")
    @SequenceGenerator(
            name = "medias_seq",
            sequenceName = "medias_seq",
            allocationSize = 50
    )
    private Long id;

    @Column(length = 500, nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(nullable = false)
    private Long size; // in bytes

    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", updatable = false)
    private LocalDateTime UpdatedAt;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
