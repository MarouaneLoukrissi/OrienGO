package com.example.oriengo.mapper;

import com.example.oriengo.model.dto.TestResultResponseDTO;
import com.example.oriengo.model.entity.TestResult;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestResultMapper {

    @Mapping(target = "testId", source = "test.id")
    @Mapping(target = "pdfId", source = "pdf.id")
    TestResultResponseDTO toDTO(TestResult testResult);

    List<TestResultResponseDTO> toDTO(List<TestResult> dtos);

    @InheritInverseConfiguration
    @Mapping(target = "test", ignore = true) // gérer séparément la relation Test si nécessaire
    @Mapping(target = "pdf", ignore = true)  // idem
    TestResult toEntity(TestResultResponseDTO dto);

}
