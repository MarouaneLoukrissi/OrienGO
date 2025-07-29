package com.example.oriengo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = "user")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tokens",
        indexes = {
                @Index(name = "idx_token_value", columnList = "token_value", unique = true),
                @Index(name = "idx_token_user_id", columnList = "user_id")
        })
public class Token implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tokens_seq")
    @SequenceGenerator(name = "tokens_seq", sequenceName = "tokens_seq", allocationSize = 50)
    private Long id;

    @Column(name = "token_value", nullable = false, unique = true, length = 512) //default length is 255
    private String tokenValue;

    private String tokenHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TokenType tokenType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt; //When a user logs out or resets their password, old tokens get revoked.

    @Column(nullable = false)
    private boolean revoked = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public boolean isActive() {
        return !revoked && expiresAt.isAfter(LocalDateTime.now());
    }
}
