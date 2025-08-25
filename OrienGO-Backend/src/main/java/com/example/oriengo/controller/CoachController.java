package com.example.oriengo.controller;

import com.example.oriengo.mapper.CoachMapper;
import com.example.oriengo.model.dto.CoachCreateDTO;
import com.example.oriengo.model.dto.CoachUpdateProfileDTO;
import com.example.oriengo.model.dto.CoachResponseDTO;
import com.example.oriengo.model.entity.Coach;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.CoachService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("api/coach")
@RequiredArgsConstructor
@Validated
public class CoachController {

    private final CoachService coachService;
    private final CoachMapper coachMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CoachResponseDTO>>> getCoaches() {
        List<Coach> coaches = coachService.getCoaches(false);
        List<CoachResponseDTO> coachResps = coachMapper.toDTO(coaches);
        ApiResponse<List<CoachResponseDTO>> response = ApiResponse.<List<CoachResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Coaches fetched successfully")
                .data(coachResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted")
    public ResponseEntity<ApiResponse<List<CoachResponseDTO>>> getDeletedCoaches() {
        List<Coach> coaches = coachService.getCoaches(true);
        List<CoachResponseDTO> coachResps = coachMapper.toDTO(coaches);
        ApiResponse<List<CoachResponseDTO>> response = ApiResponse.<List<CoachResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted coaches fetched successfully")
                .data(coachResps)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CoachResponseDTO>> getCoachById(@PathVariable Long id) {
        Coach coach = coachService.getCoachById(id, false);
        CoachResponseDTO coachResp = coachMapper.toDTO(coach);
        ApiResponse<CoachResponseDTO> response = ApiResponse.<CoachResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Coach fetched successfully")
                .data(coachResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<ApiResponse<CoachResponseDTO>> getDeletedCoachById(@PathVariable Long id) {
        Coach coach = coachService.getCoachById(id, true);
        CoachResponseDTO coachResp = coachMapper.toDTO(coach);
        ApiResponse<CoachResponseDTO> response = ApiResponse.<CoachResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted coach fetched successfully")
                .data(coachResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<CoachResponseDTO>> getCoachByEmail(@PathVariable String email) {
        Coach coach = coachService.getCoachByEmail(email, false);
        CoachResponseDTO coachResp = coachMapper.toDTO(coach);
        ApiResponse<CoachResponseDTO> response = ApiResponse.<CoachResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Coach fetched successfully")
                .data(coachResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("deleted/email/{email}")
    public ResponseEntity<ApiResponse<CoachResponseDTO>> getDeletedCoachByEmail(@PathVariable String email) {
        Coach coach = coachService.getCoachByEmail(email, true);
        CoachResponseDTO coachResp = coachMapper.toDTO(coach);
        ApiResponse<CoachResponseDTO> response = ApiResponse.<CoachResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Deleted coach fetched successfully")
                .data(coachResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteCoach(@PathVariable Long id) {
        coachService.hardDeleteCoach(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("COACH_DELETED")
                .status(204)
                .message("Coach hard deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteCoach(@PathVariable Long id) {
        coachService.softDeleteCoach(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("SUCCESS")
                .status(204)
                .message("Coach soft deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<CoachResponseDTO>> restoreCoach(@PathVariable Long id) {
        Coach coach = coachService.restoreCoach(id);
        CoachResponseDTO coachResp = coachMapper.toDTO(coach);
        ApiResponse<CoachResponseDTO> response = ApiResponse.<CoachResponseDTO>builder()
                .code("COACH_RESTORED")
                .status(200)
                .message("Coach restored successfully")
                .data(coachResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CoachResponseDTO>> createCoach(@Valid @RequestBody CoachCreateDTO coachInfo) {
        Coach coach = coachService.createCoach(coachInfo);
        CoachResponseDTO coachResp = coachMapper.toDTO(coach);
        ApiResponse<CoachResponseDTO> response = ApiResponse.<CoachResponseDTO>builder()
                .code("SUCCESS")
                .status(201)
                .message("Coach created successfully")
                .data(coachResp)
                .build();
        URI location = URI.create("/api/coach/" + coach.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CoachResponseDTO>> updateCoach(@PathVariable Long id, @Valid @RequestBody CoachCreateDTO coachInfo) {
        Coach coach = coachService.updateCoach(id, coachInfo);
        CoachResponseDTO coachResp = coachMapper.toDTO(coach);
        ApiResponse<CoachResponseDTO> response = ApiResponse.<CoachResponseDTO>builder()
                .code("COACH_UPDATED")
                .status(200)
                .message("Coach updated successfully")
                .data(coachResp)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<CoachResponseDTO>> updateProfileCoach(@PathVariable Long id, @Valid @RequestBody CoachUpdateProfileDTO coachInfo) {
        Coach coach = coachService.updateProfileCoach(id, coachInfo);
        CoachResponseDTO coachResp = coachMapper.toDTO(coach);
        ApiResponse<CoachResponseDTO> response = ApiResponse.<CoachResponseDTO>builder()
                .code("COACH_UPDATED")
                .status(200)
                .message("Coach updated successfully")
                .data(coachResp)
                .build();
        return ResponseEntity.ok(response);
    }
}
