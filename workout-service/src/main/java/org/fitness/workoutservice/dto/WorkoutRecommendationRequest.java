package org.fitness.workoutservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.fitness.workoutservice.model.DifficultyLevel;
import org.fitness.workoutservice.model.WorkoutType;

@Data
public class WorkoutRecommendationRequest {
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    private String fitnessGoal;
    private String fitnessLevel;
    private Integer age;
    private Double weight;
    private Double height;
    private String gender;
    private WorkoutType preferredWorkoutType;
    private DifficultyLevel difficulty;
    private Integer durationMinutes;
    private String equipment; // Available equipment
    private String healthConditions; // Any health restrictions
}
