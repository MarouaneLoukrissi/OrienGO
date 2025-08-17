package com.example.oriengo.model.entity;

import com.example.oriengo.model.enumeration.AdminLevel;
import com.example.oriengo.model.enumeration.Department;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true, exclude = "createdAdmins")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "admins")
public class Admin extends User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name = "admin_level", nullable = false)
    private AdminLevel adminLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    //    @JsonIgnore
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Admin createdBy;

    @JsonManagedReference
    @Builder.Default
    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private Set<Admin> createdAdmins = new HashSet<>();

    @PrePersist
    private void validateCreatedBy() {
        if (this.adminLevel != AdminLevel.MANAGER && this.createdBy == null) {
            throw new IllegalStateException("Only SUPER_ADMIN can have a null createdBy");
        }
    }

    @PreRemove
    private void preventDeleteIfHasChildren() {
        if (!createdAdmins.isEmpty()) {
            throw new IllegalStateException("Cannot delete an admin who has created other admins.");
        }
    }
}
