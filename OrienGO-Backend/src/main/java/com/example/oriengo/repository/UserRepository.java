package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Role;
import com.example.oriengo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByIsDeleted(boolean deleted);

    Optional<User> findByIdAndIsDeleted(Long id, boolean deleted);

    Optional<User> findByEmailAndIsDeleted(String email, boolean deleted);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role AND u.isDeleted = :deleted")
    List<User> findAllByRoleAndIsDeleted(@Param("role") Role role, @Param("deleted") boolean deleted);

    @Query("SELECT COUNT(DISTINCT u) FROM User u " +
            "JOIN u.roles r " +
            "WHERE u.isDeleted = :deleted " +
            "AND r.name IN (:roleNames)")
    long countUsersByDeletedAndRoles(@Param("deleted") boolean deleted,
                                     @Param("roleNames") List<String> roleNames);
}
