package org.fitness.workoutservice.dto;

import lombok.Data;
import org.fitness.workoutservice.model.DifficultyLevel;
import org.fitness.workoutservice.model.WorkoutType;

import java.time.LocalDateTime;

@Data
public class WorkoutResponse {
    
    private String id;
    private String userId;
    private String name;
    private String description;
    private WorkoutType type;
    private DifficultyLevel difficulty;
    private Integer durationMinutes;
    private Integer estimatedCalories;
    private String exercises;
    private Boolean aiGenerated;
    private LocalDateTime createdAt;
}
