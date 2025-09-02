package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.PersonalizedJobRequestDto;
import com.example.oriengo.model.dto.PersonalizedJobResponseDto;
import com.example.oriengo.model.entity.PersonalizedJob;
import com.example.oriengo.model.entity.StudentPersonalizedJobLink;
import com.example.oriengo.model.enumeration.LinkType;
import com.example.oriengo.repository.StudentPersonalizedJobLinkRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonalizedJobMapper {

    default PersonalizedJobResponseDto toResponseDtoWithLinks(PersonalizedJob entity, Long studentId, StudentPersonalizedJobLinkRepository linkRepo) {
        PersonalizedJobResponseDto dto = toResponseDto(entity); // map basic fields
        if (studentId != null) {
            dto.setLinkTypes(linkRepo.findByStudent_IdAndPersonalizedJob_Id(studentId, entity.getId())
                    .stream()
                    .map(StudentPersonalizedJobLink::getType)
                    .toList());
        }
        return dto;
    }

    default List<PersonalizedJobResponseDto> toResponseDtoListWithLinks(List<PersonalizedJob> entities, Long studentId, StudentPersonalizedJobLinkRepository linkRepo) {
        return entities.stream()
                .map(job -> toResponseDtoWithLinks(job, studentId, linkRepo))
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jobRecommendation", ignore = true)
    PersonalizedJob toEntity(PersonalizedJobRequestDto dto);

    @Mapping(target = "jobRecommendationId", expression = "java(entity.getJobRecommendation() != null ? entity.getJobRecommendation().getId() : null)")
    PersonalizedJobResponseDto toResponseDto(PersonalizedJob entity);

    List<PersonalizedJobResponseDto> toResponseDtoList(List<PersonalizedJob> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jobRecommendation", ignore = true)
    void updateEntityFromDto(PersonalizedJobRequestDto dto, @MappingTarget PersonalizedJob entity);

    // ====================== New method ======================
    default PersonalizedJobResponseDto toResponseDto(PersonalizedJob entity, Long studentId) {
        PersonalizedJobResponseDto dto = toResponseDto(entity); // use existing mapping
        if (studentId != null && entity.getStudentLinks() != null) {
            List<LinkType> types = entity.getStudentLinks().stream()
                    .filter(link -> link.getStudent().getId().equals(studentId))
                    .map(StudentPersonalizedJobLink::getType)
                    .toList();
            dto.setLinkTypes(types.isEmpty() ? List.of(LinkType.NORMAL) : types);
        } else {
            dto.setLinkTypes(List.of(LinkType.NORMAL));
        }
        return dto;
    }

    default List<PersonalizedJobResponseDto> toResponseDtoList(List<PersonalizedJob> entities, Long studentId) {
        return entities.stream()
                .map(job -> toResponseDto(job, studentId))
                .toList();
    }
}