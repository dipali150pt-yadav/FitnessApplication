package org.fitness.workoutservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fitness.workoutservice.dto.CreateWorkoutRequest;
import org.fitness.workoutservice.dto.WorkoutRecommendationRequest;
import org.fitness.workoutservice.dto.WorkoutResponse;
import org.fitness.workoutservice.service.WorkoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {
    
    private final WorkoutService workoutService;
    
    @PostMapping("/recommend")
    public ResponseEntity<WorkoutResponse> generateRecommendation(
            @Valid @RequestBody WorkoutRecommendationRequest request) {
        WorkoutResponse response = workoutService.generateWorkoutRecommendation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping
    public ResponseEntity<WorkoutResponse> createWorkout(
            @Valid @RequestBody CreateWorkoutRequest request) {
        WorkoutResponse response = workoutService.createWorkout(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutResponse> getWorkoutById(@PathVariable String workoutId) {
        WorkoutResponse response = workoutService.getWorkoutById(workoutId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkoutResponse>> getUserWorkouts(@PathVariable String userId) {
        List<WorkoutResponse> workouts = workoutService.getUserWorkouts(userId);
        return ResponseEntity.ok(workouts);
    }
    
    @GetMapping("/user/{userId}/ai-generated")
    public ResponseEntity<List<WorkoutResponse>> getAIGeneratedWorkouts(@PathVariable String userId) {
        List<WorkoutResponse> workouts = workoutService.getAIGeneratedWorkouts(userId);
        return ResponseEntity.ok(workouts);
    }
    
    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String workoutId) {
        workoutService.deleteWorkout(workoutId);
        return ResponseEntity.noContent().build();
    }
}
