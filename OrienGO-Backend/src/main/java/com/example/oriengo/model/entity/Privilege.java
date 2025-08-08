package com.example.oriengo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "roles")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "privileges")
public class Privilege implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privileges_seq")
    @SequenceGenerator(
            name = "privileges_seq",
            sequenceName = "privileges_seq",
            allocationSize = 50
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default
    @ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

}
