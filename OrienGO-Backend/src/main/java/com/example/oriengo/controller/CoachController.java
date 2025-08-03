package com.example.oriengo.controller;

import com.example.oriengo.model.dto.CoachCreateDTO;
import com.example.oriengo.model.entity.Coach;
import com.example.oriengo.service.CoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/coach")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;

    @GetMapping
    public ResponseEntity<List<Coach>> getCoachs(){
        List<Coach> coachs = coachService.getCoachs(false);
        return ResponseEntity.ok(coachs);
    }

    @GetMapping("deleted")
    public ResponseEntity<List<Coach>> getDeletedCoachs(){
        List<Coach> coachs = coachService.getCoachs(true);
        return ResponseEntity.ok(coachs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coach> getCoachById(@PathVariable Long id){
        Coach coach = coachService.getCoachById(id, false);
        return ResponseEntity.ok(coach);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<Coach> getDeletedCoachById(@PathVariable Long id){
        Coach coach = coachService.getCoachById(id, true);
        return ResponseEntity.ok(coach);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Coach> getCoachByEmail(@PathVariable String email){
        Coach coach = coachService.getCoachByEmail(email, false);
        return ResponseEntity.ok(coach);
    }

    @GetMapping("deleted/email/{email}")
    public ResponseEntity<Coach> getDeletedCoachByEmail(@PathVariable String email){
        Coach coach = coachService.getCoachByEmail(email, true);
        return ResponseEntity.ok(coach);
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> hardDeleteCoach(@PathVariable Long id){
        coachService.hardDeleteCoach(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteCoach(@PathVariable Long id){
        coachService.softDeleteCoach(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Coach> createCoach(@Validated @RequestBody CoachCreateDTO coachInfo){
        Coach coach = coachService.createCoach(coachInfo);
        URI location = URI.create("/api/coach/" + coach.getId());
        return ResponseEntity.created(location).body(coach);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coach> updateCoach(@PathVariable Long id, @Validated @RequestBody CoachCreateDTO coachInfo){
        Coach coach = coachService.updateCoach(id, coachInfo);
        return ResponseEntity.ok(coach);
    }

}
