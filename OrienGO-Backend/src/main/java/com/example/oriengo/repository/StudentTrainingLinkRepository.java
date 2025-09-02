package com.example.oriengo.repository;

import com.example.oriengo.model.entity.Student;
import com.example.oriengo.model.entity.StudentTrainingLink;
import com.example.oriengo.model.entity.Training;
import com.example.oriengo.model.enumeration.LinkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentTrainingLinkRepository extends JpaRepository<StudentTrainingLink, Long> {

    // Find by student, training, and type
    Optional<StudentTrainingLink> findByStudentAndTrainingAndType(Student student, Training training, LinkType type);

    // Optional: if you want to find *any* link regardless of type
    Optional<StudentTrainingLink> findByStudentAndTraining(Student student, Training training);

    // Optional: existence check
    boolean existsByStudentAndTrainingAndType(Student student, Training training, LinkType type);

    // Optional: direct deletion
    void deleteByStudentAndTrainingAndType(Student student, Training training, LinkType type);

    List<StudentTrainingLink> findAllByStudent_IdAndTraining_Id(Long studentId, Long trainingId);

    List<StudentTrainingLink> findAllByType(LinkType type);
}
