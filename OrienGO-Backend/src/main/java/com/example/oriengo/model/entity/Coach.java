package com.example.oriengo.model.entity;

import com.example.oriengo.model.enumeration.AccountPrivacy;
import com.example.oriengo.model.enumeration.CoachSpecialization;
import com.example.oriengo.model.enumeration.MessagePermission;
import com.example.oriengo.model.enumeration.VisibilityStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("COACH")
@Table(name = "coaches")
public class Coach extends User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name="profile_visibility", nullable = false)
    private VisibilityStatus profileVisibility = VisibilityStatus.PUBLIC;

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CoachStudentConnection> connections = new HashSet<>();

    @Embedded
    private Location location;

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "coach", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Conversation> conversations = new HashSet<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "message_permission", nullable = false)
    private MessagePermission messagePermission = MessagePermission.NETWORK;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountPrivacy accountPrivacy = AccountPrivacy.PUBLIC;

    @Enumerated(EnumType.STRING)
    private CoachSpecialization specialization;

    private Double rate;

    @Column(nullable = true, length = 500)
    private String expertise; // e.g., "Career Development, Personal Growth, Goal Setting"

    @Column(nullable = true, length = 200)
    private String services; // e.g., "Available for 1-on-1 sessions"

    @Column(nullable = true, length = 500)
    private String availability; // e.g., "Monday - Friday: 9 AM - 6 PM; Weekend: By appointment"
}
