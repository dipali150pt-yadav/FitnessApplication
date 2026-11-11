package org.fitness.workoutservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "workouts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workout {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkoutType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficulty;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @Column(name = "estimated_calories")
    private Integer estimatedCalories;
    
    @Column(columnDefinition = "TEXT")
    private String exercises; // JSON string of exercises
    
    @Column(name = "ai_generated")
    private Boolean aiGenerated = false;
    
    @Column(name = "ai_prompt", columnDefinition = "TEXT")
    private String aiPrompt;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
