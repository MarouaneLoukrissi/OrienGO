package com.example.oriengo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notification_settings",
        uniqueConstraints = @UniqueConstraint(columnNames = "user_id"),
        indexes = {@Index(name = "idx_notif_settings_user", columnList = "user_id")}
)
public class NotificationSettings implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notif_settings_seq")
    @SequenceGenerator(name = "notif_settings_seq", sequenceName = "notif_settings_seq", allocationSize = 50)
    private Long id;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Builder.Default
    @Column(nullable = false)
    private boolean testCompletedFast = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean testCompletedFull = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean connectionRequests = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean profileViews = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean newMessages = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean addedToGroup = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean newJobs = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean testReminders = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean receiveEmailNotifications = true;

}
