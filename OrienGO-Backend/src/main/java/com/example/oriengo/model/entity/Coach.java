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

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "coach", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Conversation> conversations = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "message_permission", nullable = false)
    private MessagePermission messagePermission = MessagePermission.NETWORK;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountPrivacy accountPrivacy = AccountPrivacy.PUBLIC;

    public boolean hasConnection(Student student) {
        return connections.stream()
                .anyMatch(c -> c.getStudent().equals(student));
    }

    public CoachStudentConnection connectTo(Student student) {
        CoachStudentConnection connection = CoachStudentConnection.builder()
                .coach(this)
                .student(student)
                .build();
        this.connections.add(connection);
        student.getConnections().add(connection);
        return connection;
    }

    public void disconnectFrom(Student student) {
        this.connections.removeIf(c -> c.getStudent().equals(student));
        student.getConnections().removeIf(c -> c.getCoach().equals(this));
    }

}
