package com.example.oriengo.model.entity;

import com.example.oriengo.model.enumeration.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"recipient"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notification_user", columnList = "recipient_id"),
        @Index(name = "idx_notification_read", columnList = "read")
})
public class Notification implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notif_seq")
    @SequenceGenerator(name = "notif_seq", sequenceName = "notif_seq", allocationSize = 50)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private NotificationType type;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "message", nullable = false, length = 2000)
    private String message;

    @Column(name = "url", length = 512)
    private String url; // for redirection in frontend

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean read = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient; // can be Student or CoachRepository

    @Column(nullable = false)
    private boolean deleted = false;

    @Builder.Default
    @Column(name = "send_email", nullable = false)
    private boolean sendEmail = true; // set from user preferences

//    @Version
//    private Long version;

}
