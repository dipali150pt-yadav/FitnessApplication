package org.fitness.activityservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fitness.activityservice.model.ActivityType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityEvent implements Serializable {
    
    private Long activityId;
    private String userId;
    private String activityName;
    private ActivityType activityType;
    private Integer durationMinutes;
    private Integer caloriesBurned;
    private LocalDateTime activityDate;
    private LocalDateTime eventTimestamp;
}
