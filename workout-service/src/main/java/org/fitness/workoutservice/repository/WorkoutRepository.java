package org.fitness.workoutservice.repository;

import org.fitness.workoutservice.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, String> {
    
    List<Workout> findByUserId(String userId);
    
    List<Workout> findByUserIdAndAiGenerated(String userId, Boolean aiGenerated);
}
