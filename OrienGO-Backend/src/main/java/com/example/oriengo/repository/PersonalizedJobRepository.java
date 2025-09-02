package com.example.oriengo.repository;

import com.example.oriengo.model.entity.PersonalizedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalizedJobRepository extends JpaRepository<PersonalizedJob, Long> {
    
    Optional<List<PersonalizedJob>> findByJobRecommendationId(Long jobRecommendationId);
    
    List<PersonalizedJob> findByHighlightedTrue();

    @Query("SELECT pj FROM PersonalizedJob pj " +
            "JOIN pj.studentLinks l " +
            "WHERE l.student.id = :studentId")
    List<PersonalizedJob> findByStudentId(@Param("studentId") Long studentId);

    @Query("""
        SELECT pj
        FROM PersonalizedJob pj
        WHERE pj.id IN (
            SELECT MIN(pj2.id)
            FROM PersonalizedJob pj2
            WHERE pj2.jobRecommendation.id IN :ids
              AND pj2.softDeleted = false
            GROUP BY
              LOWER(TRIM(COALESCE(pj2.title, ''))),
              LOWER(TRIM(COALESCE(pj2.companyName, ''))),
              LOWER(TRIM(COALESCE(pj2.location, '')))
        )
    """)
    List<PersonalizedJob> findDistinctNormalizedByRecommendationIds(@Param("ids") List<Long> ids);

    List<PersonalizedJob> findByJobRecommendationIdIn(List<Long> jobRecommendationIds);

    @Query("SELECT pj FROM PersonalizedJob pj " +
            "JOIN pj.studentLinks l " +
            "WHERE l.student.id = :studentId " +
            "AND l.type = :type")
    List<PersonalizedJob> findByStudentIdAndLinkType(@Param("studentId") Long studentId,
                                                     @Param("type") com.example.oriengo.model.enumeration.LinkType type);

    @Query("SELECT pj FROM PersonalizedJob pj " +
            "JOIN pj.studentLinks l " +
            "WHERE l.type = :type")
    List<PersonalizedJob> findByLinkType(@Param("type") com.example.oriengo.model.enumeration.LinkType type);
}