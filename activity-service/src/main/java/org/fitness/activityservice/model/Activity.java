package org.fitness.activityservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_activity_date", columnList = "activity_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "activity_type")
    private ActivityType activityType;
    
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;
    
    @Column(name = "calories_burned")
    private Integer caloriesBurned;
    
    @Column(name = "distance_km")
    private Double distanceKm;
    
    @Column(name = "average_heart_rate")
    private Integer averageHeartRate;
    
    @Enumerated(EnumType.STRING)
    private IntensityLevel intensity;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "activity_date", nullable = false)
    private LocalDateTime activityDate;
    
    @Column(name = "workout_id")
    private String workoutId; // Link to workout service
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
