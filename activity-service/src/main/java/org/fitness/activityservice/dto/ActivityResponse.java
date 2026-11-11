package org.fitness.activityservice.dto;

import lombok.Data;
import org.fitness.activityservice.model.ActivityType;
import org.fitness.activityservice.model.IntensityLevel;

import java.time.LocalDateTime;

@Data
public class ActivityResponse {
    
    private Long id;
    private String userId;
    private String name;
    private String description;
    private ActivityType activityType;
    private Integer durationMinutes;
    private Integer caloriesBurned;
    private Double distanceKm;
    private Integer averageHeartRate;
    private IntensityLevel intensity;
    private String notes;
    private LocalDateTime activityDate;
    private String workoutId;
    private LocalDateTime createdAt;
}
