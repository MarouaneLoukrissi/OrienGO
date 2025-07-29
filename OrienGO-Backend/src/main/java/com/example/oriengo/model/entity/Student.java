package com.example.oriengo.model.entity;

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
@DiscriminatorValue("STUDENT")
@Table(name = "students")
public class Student extends User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(length = 100)
    private String school;

    @Column(name = "field_of_study", length = 100)
    private String fieldOfStudy;

    @Enumerated(EnumType.STRING)
    @Column(name = "education_level", nullable = false)
    private EducationLevel educationLevel;

    @Embedded
    private Location location;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name="profile_visibility", nullable = false)
    private VisibilityStatus profileVisibility = VisibilityStatus.PUBLIC;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Set<CoachStudentConnection> connections = new HashSet<>();

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "student", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Conversation> conversations = new HashSet<>();

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Test> tests = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "message_permission", nullable = false)
    private MessagePermission messagePermission = MessagePermission.NETWORK;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountPrivacy accountPrivacy = AccountPrivacy.PUBLIC;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<StudentJobLink> jobLinks = new HashSet<>();

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<StudentTrainingLink> trainingLinks = new HashSet<>();

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<StudentPersonalizedJobLink> personalizedJobLinks = new HashSet<>();

    public boolean hasConnection(Coach coach) {
        return connections.stream().anyMatch(c -> c.getCoach().equals(coach));
    }

    public CoachStudentConnection connectTo(Coach coach) {
        CoachStudentConnection connection = CoachStudentConnection.builder()
                .coach(coach)
                .student(this)
                .build();
        this.connections.add(connection);
        coach.getConnections().add(connection);
        return connection;
    }

    public void disconnectFrom(Coach coach) {
        this.connections.removeIf(c -> c.getCoach().equals(coach));
        coach.getConnections().removeIf(c -> c.getStudent().equals(this));
    }

}
