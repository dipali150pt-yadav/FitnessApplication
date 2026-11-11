package org.fitness.activityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStatsResponse {
    
    private String userId;
    private Long totalActivities;
    private Integer totalDurationMinutes;
    private Integer totalCaloriesBurned;
    private Double totalDistanceKm;
    private Double averageActivityDuration;
    private Integer averageCaloriesPerActivity;
}
