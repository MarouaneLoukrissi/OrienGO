package com.example.oriengo.model.entity;

import com.example.oriengo.model.enumeration.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private Set<CoachStudentConnection> connections = new HashSet<>();

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "student", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Conversation> conversations = new HashSet<>();

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Test> tests = new HashSet<>();


    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "message_permission", nullable = false)
    private MessagePermission messagePermission = MessagePermission.NETWORK;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountPrivacy accountPrivacy = AccountPrivacy.PUBLIC;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<StudentJobLink> jobLinks = new HashSet<>();

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<StudentTrainingLink> trainingLinks = new HashSet<>();

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<StudentPersonalizedJobLink> personalizedJobLinks = new HashSet<>();

}
