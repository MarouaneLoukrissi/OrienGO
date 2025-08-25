package com.example.oriengo.controller;

import com.example.oriengo.mapper.CoachStudentConnectionMapper;
import com.example.oriengo.model.dto.*;
import com.example.oriengo.model.entity.CoachStudentConnection;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.CoachStudentConnectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("api/coach-student")
@RequiredArgsConstructor
@Validated
public class CoachStudentConnectionController {

    private final CoachStudentConnectionService coachStudentConnectionService;
    private final CoachStudentConnectionMapper coachStudentConnectionMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CoachStudentConnectionResponseDTO>>> getAllCoachStudentConnections() {
        List<CoachStudentConnection> coachStudentConnections = coachStudentConnectionService.findAll();
        List<CoachStudentConnectionResponseDTO> coachStudentConnectionDtos = coachStudentConnections.stream().map(coachStudentConnectionMapper::toDTO).collect(Collectors.toList());

        ApiResponse<List<CoachStudentConnectionResponseDTO>> response = ApiResponse.<List<CoachStudentConnectionResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("CoachStudentConnections fetched successfully")
                .data(coachStudentConnectionDtos)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CoachStudentConnectionResponseDTO>> getCoachStudentConnectionById(@PathVariable Long id) {
        CoachStudentConnection coachStudentConnection = coachStudentConnectionService.findById(id);
        CoachStudentConnectionResponseDTO coachStudentConnectionDto = coachStudentConnectionMapper.toDTO(coachStudentConnection);

        ApiResponse<CoachStudentConnectionResponseDTO> response = ApiResponse.<CoachStudentConnectionResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("CoachStudentConnection fetched successfully")
                .data(coachStudentConnectionDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponse<CoachStudentConnectionResponseDTO>> createCoachStudentConnection(@Valid @RequestBody CoachStudentConnectionCreateDTO requestDto) {
        CoachStudentConnection coachStudentConnection = coachStudentConnectionService.create(requestDto);
        CoachStudentConnectionResponseDTO coachStudentConnectionDto = coachStudentConnectionMapper.toDTO(coachStudentConnection);

        ApiResponse<CoachStudentConnectionResponseDTO> response = ApiResponse.<CoachStudentConnectionResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("CoachStudentConnection created successfully")
                .data(coachStudentConnectionDto)
                .build();
        URI location = URI.create("/api/coachStudentConnection/" + coachStudentConnection.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<CoachStudentConnectionResponseDTO>> updateCoachStudentConnection(@PathVariable Long id, @Valid @RequestBody CoachStudentConnectionUpdateDTO requestDto) {
        CoachStudentConnection updatedCoachStudentConnection = coachStudentConnectionService.update(id, requestDto);
        CoachStudentConnectionResponseDTO coachStudentConnectionDto = coachStudentConnectionMapper.toDTO(updatedCoachStudentConnection);

        ApiResponse<CoachStudentConnectionResponseDTO> response = ApiResponse.<CoachStudentConnectionResponseDTO>builder()
                .code("JOB_UPDATED")
                .status(200)
                .message("CoachStudentConnection updated successfully")
                .data(coachStudentConnectionDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> deleteCoachStudentConnection(@PathVariable Long id) {
        coachStudentConnectionService.deleteById(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("JOB_DELETED")
                .status(204)
                .message("CoachStudentConnection deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("student/{studentId}/status/{status}/{requestedBy}")
    public ResponseEntity<ApiResponse<List<CoachStudentConnectionResponseDTO>>> getByStudentIdAndStatusAndRequestedBy(@PathVariable Long studentId, @PathVariable String status, @PathVariable String requestedBy) {
        List<CoachStudentConnection> coachStudentConnections = coachStudentConnectionService.findByStudentIdAndStatusAndRequestedBy(studentId, status, requestedBy);
        List<CoachStudentConnectionResponseDTO> coachStudentConnectionDtos = coachStudentConnections.stream().map(coachStudentConnectionMapper::toDTO).collect(Collectors.toList());

        ApiResponse<List<CoachStudentConnectionResponseDTO>> response = ApiResponse.<List<CoachStudentConnectionResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Coach student connections by category fetched successfully")
                .data(coachStudentConnectionDtos)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("coach/{coachId}/status/{status}/{requestedBy}")
    public ResponseEntity<ApiResponse<List<CoachStudentConnectionResponseDTO>>> getByCoachIdAndStatusAndRequestedBy(@PathVariable Long coachId, @PathVariable String status, @PathVariable String requestedBy) {
        List<CoachStudentConnection> coachStudentConnections = coachStudentConnectionService.findByCoachIdAndStatusAndRequestedBy(coachId, status, requestedBy);
        List<CoachStudentConnectionResponseDTO> coachStudentConnectionDtos = coachStudentConnections.stream().map(coachStudentConnectionMapper::toDTO).collect(Collectors.toList());

        ApiResponse<List<CoachStudentConnectionResponseDTO>> response = ApiResponse.<List<CoachStudentConnectionResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("CoachStudentConnections by category fetched successfully")
                .data(coachStudentConnectionDtos)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("student/{studentId}/status/{status}/count")
    public ResponseEntity<ApiResponse<Long>> countByStudentIdAndStatus(
            @PathVariable Long studentId,
            @PathVariable String status) {

        long count = coachStudentConnectionService.countByStudentIdAndStatus(studentId, status);

        ApiResponse<Long> response = ApiResponse.<Long>builder()
                .code("SUCCESS")
                .status(200)
                .message("Coach student connections count fetched successfully")
                .data(count)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("coach/{coachId}/status/{status}/count")
    public ResponseEntity<ApiResponse<Long>> countByCoachIdAndStatus(
            @PathVariable Long coachId,
            @PathVariable String status) {

        long count = coachStudentConnectionService.countByCoachIdAndStatus(coachId, status);

        ApiResponse<Long> response = ApiResponse.<Long>builder()
                .code("SUCCESS")
                .status(200)
                .message("CoachStudentConnections count fetched successfully")
                .data(count)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("coach/{coachId}/status/{status}/dominant-profile")
    public ResponseEntity<ApiResponse<TestResultAverageDTO>> getCoacheesDominantProfile(
            @PathVariable Long coachId,
            @PathVariable String status) {

        TestResultAverageDTO dominantProfile = coachStudentConnectionService
                .getDominantProfileByCoachIdStatus(coachId, status);

        ApiResponse<TestResultAverageDTO> response = ApiResponse.<TestResultAverageDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Dominant profile fetched successfully")
                .data(dominantProfile)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("coach/{coachId}/status/{status}/average-profiles")
    public ResponseEntity<ApiResponse<TestResultProfilesDTO>> getCoacheesAverageProfiles(
            @PathVariable Long coachId,
            @PathVariable String status) {

        // Call service method to get average profiles
        TestResultProfilesDTO averageProfiles = coachStudentConnectionService
                .getAverageProfilesByCoachIdStatus(coachId, status);

        // Wrap in API response
        ApiResponse<TestResultProfilesDTO> response = ApiResponse.<TestResultProfilesDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Average profiles fetched successfully")
                .data(averageProfiles)
                .build();

        return ResponseEntity.ok(response);
    }

}
