package org.fitness.workoutservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.fitness.workoutservice.model.DifficultyLevel;
import org.fitness.workoutservice.model.WorkoutType;

@Data
public class CreateWorkoutRequest {
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Workout name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Workout type is required")
    private WorkoutType type;
    
    @NotNull(message = "Difficulty level is required")
    private DifficultyLevel difficulty;
    
    private Integer durationMinutes;
    private Integer estimatedCalories;
    private String exercises;
}
