package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findByIsDeleted(boolean deleted);
    Optional<Admin> findByIdAndIsDeleted(Long id, boolean deleted);
    Optional<Admin> findByEmailAndIsDeleted(String email, boolean deleted);

    boolean existsByEmailAndIsDeletedFalse(String originalEmail);

    @Query("SELECT a FROM Admin a WHERE a.id = :id")
    Optional<Admin> findByIdIncludingDeleted(@Param("id") Long id);
}
