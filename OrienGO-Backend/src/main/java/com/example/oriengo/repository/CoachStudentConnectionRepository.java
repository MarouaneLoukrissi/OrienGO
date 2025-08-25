package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Coach;
import com.example.oriengo.model.entity.CoachStudentConnection;
import com.example.oriengo.model.entity.Student;
import com.example.oriengo.model.enumeration.ConnectionStatus;
import com.example.oriengo.model.enumeration.RequestInitiator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachStudentConnectionRepository extends JpaRepository<CoachStudentConnection, Long> {

    @Query("SELECT c FROM CoachStudentConnection c " +
            "WHERE c.coach.id = :coachId " +
            "AND c.status = :status")
    List<CoachStudentConnection> findByCoachIdAndStatus(@Param("coachId") Long coachId,
                                                        @Param("status") ConnectionStatus status);

    @Query("SELECT c FROM CoachStudentConnection c " +
            "WHERE c.student.id = :studentId " +
            "AND c.status = :status " +
            "AND c.requestedBy = :requestedBy")
    List<CoachStudentConnection> findByStudentIdAndStatusAndRequestedBy(
            @Param("studentId") Long studentId,
            @Param("status") ConnectionStatus status,
            @Param("requestedBy") RequestInitiator requestedBy
    );

    @Query("SELECT c FROM CoachStudentConnection c " +
            "WHERE c.coach.id = :coachId " +
            "AND c.status = :status " +
            "AND c.requestedBy = :requestedBy")
    List<CoachStudentConnection> findByCoachIdAndStatusAndRequestedBy(
            @Param("coachId") Long coachId,
            @Param("status") ConnectionStatus status,
            @Param("requestedBy") RequestInitiator requestedBy
    );


    boolean existsByCoachAndStudent(Coach coach, Student student);

    @Query("SELECT COUNT(c) FROM CoachStudentConnection c " +
            "WHERE c.student.id = :studentId " +
            "AND c.status = :status ")
    long countByStudentIdAndStatusAndRequestedBy(
            @Param("studentId") Long studentId,
            @Param("status") ConnectionStatus status
    );

    @Query("SELECT COUNT(c) FROM CoachStudentConnection c " +
            "WHERE c.coach.id = :coachId " +
            "AND c.status = :status ")
    long countByCoachIdAndStatusAndRequestedBy(
            @Param("coachId") Long coachId,
            @Param("status") ConnectionStatus status
    );

    @Query("SELECT c.student.id FROM CoachStudentConnection c " +
            "WHERE c.coach.id = :coachId " +
            "AND c.status = :status ")
    List<Long> findStudentIdsByCoachIdAndStatusAndRequestedBy(
            @Param("coachId") Long coachId,
            @Param("status") ConnectionStatus status
    );

}
