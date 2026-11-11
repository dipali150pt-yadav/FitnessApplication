package org.fitness.activityservice.repository;

import org.fitness.activityservice.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    
    List<Activity> findByUserId(String userId);
    
    List<Activity> findByUserIdOrderByActivityDateDesc(String userId);
    
    List<Activity> findByUserIdAndActivityDateBetween(
            String userId, 
            LocalDateTime startDate, 
            LocalDateTime endDate
    );
    
    @Query("SELECT COUNT(a) FROM Activity a WHERE a.userId = :userId")
    Long countByUserId(@Param("userId") String userId);
    
    @Query("SELECT SUM(a.durationMinutes) FROM Activity a WHERE a.userId = :userId")
    Integer sumDurationByUserId(@Param("userId") String userId);
    
    @Query("SELECT SUM(a.caloriesBurned) FROM Activity a WHERE a.userId = :userId")
    Integer sumCaloriesByUserId(@Param("userId") String userId);
    
    @Query("SELECT SUM(a.distanceKm) FROM Activity a WHERE a.userId = :userId")
    Double sumDistanceByUserId(@Param("userId") String userId);
    
    @Query("SELECT AVG(a.durationMinutes) FROM Activity a WHERE a.userId = :userId")
    Double avgDurationByUserId(@Param("userId") String userId);
}
