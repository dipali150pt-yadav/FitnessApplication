package org.fitness.activityservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.fitness.activityservice.model.ActivityType;
import org.fitness.activityservice.model.IntensityLevel;

import java.time.LocalDateTime;

@Data
public class CreateActivityRequest {
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Activity name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Activity type is required")
    private ActivityType activityType;
    
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationMinutes;
    
    private Integer caloriesBurned;
    private Double distanceKm;
    private Integer averageHeartRate;
    private IntensityLevel intensity;
    private String notes;
    private String workoutId;
    
    @NotNull(message = "Activity date is required")
    private LocalDateTime activityDate;
}
