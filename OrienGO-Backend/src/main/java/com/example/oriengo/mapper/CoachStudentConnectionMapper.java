package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.CoachStudentConnectionCreateDTO;
import com.example.oriengo.model.dto.CoachStudentConnectionResponseDTO;
import com.example.oriengo.model.dto.CoachStudentConnectionUpdateDTO;
import com.example.oriengo.model.entity.CoachStudentConnection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoachStudentConnectionMapper {

    @Mapping(target = "coach.id", source = "coachId")
    @Mapping(target = "student.id", source = "studentId")
    @Mapping(target = "requestedBy", source = "requestedBy") // 👈 add this
    CoachStudentConnection toEntity(CoachStudentConnectionCreateDTO dto);

    @Mapping(target = "coachId", source = "coach.id")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "requestedBy", source = "requestedBy") // 👈 also expose it in DTO
    CoachStudentConnectionResponseDTO toDTO(CoachStudentConnection entity);

    List<CoachStudentConnectionResponseDTO> toDTO(List<CoachStudentConnection> connections);

    @Mapping(target = "coach", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "requestedBy", ignore = true) // 👈 maybe ignore in updates
    void updateEntityFromDto(CoachStudentConnectionUpdateDTO dto, @MappingTarget CoachStudentConnection entity);


}
