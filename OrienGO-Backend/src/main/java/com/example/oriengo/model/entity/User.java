package com.example.oriengo.model.entity;

import com.example.oriengo.model.enumeration.GenderType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"roles", "tokens", "sentMessages", "notificationSettings", "notifications"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING) //by default called DTYPE and of type String
@Table(name = "users",
        indexes = {
                @Index(name = "idx_users_email", columnList = "email", unique = true),
                @Index(name = "idx_users_last_seen", columnList = "last_seen"),
                @Index(name = "idx_users_enabled", columnList = "enabled"),
                @Index(name = "idx_users_suspended", columnList = "suspended")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_users_email", columnNames = {"email"})
        }
)
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(
            name = "users_seq",
            sequenceName = "users_seq",
            allocationSize = 50
    )
    private Long id;

    @Column(name="first_name",nullable = false, length = 50)
    private String firstName;

    @Column(name="last_name", nullable = false, length = 50)
    private String lastName;

    private int age;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name="phone_number", length = 20)
    private String phoneNumber;

    @Column(unique = true, nullable = false, length = 365)
    private String email;

    @Column(nullable = false, length = 255)
    private transient String password;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = false;

    @Builder.Default
    @Column(name="token_expired", nullable = false)
    private boolean tokenExpired = false;

    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="last_seen")
    private LocalDateTime lastSeen;

    @Builder.Default
    @Column(name="suspended", nullable = false)
    private boolean suspended = false;

    @Column(name="suspension_reason", length = 255)
    private String suspensionReason;

    @Column(name = "suspended_until")
    private LocalDateTime suspendedUntil;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Token> tokens = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Media> mediaFiles = new HashSet<>(); //photo_profile, cover_photo, pdf, ...

//    @Builder.Default
//    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Message> sentMessages = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Notification> notifications = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, optional = false)
    private NotificationSettings notificationSettings;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Transient
    public boolean isOnline() {
        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now().minusMinutes(1));
    }

//    @Version
//    @Column(name = "version", nullable = false)
//    private Long version;

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(r -> r.getName().equalsIgnoreCase(roleName));
    }
}