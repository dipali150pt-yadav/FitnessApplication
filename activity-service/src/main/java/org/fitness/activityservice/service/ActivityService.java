package org.fitness.activityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fitness.activityservice.dto.ActivityResponse;
import org.fitness.activityservice.dto.ActivityStatsResponse;
import org.fitness.activityservice.dto.CreateActivityRequest;
import org.fitness.activityservice.exception.ResourceNotFoundException;
import org.fitness.activityservice.messaging.ActivityEventPublisher;
import org.fitness.activityservice.model.Activity;
import org.fitness.activityservice.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {
    
    private final ActivityRepository activityRepository;
    private final ActivityEventPublisher eventPublisher;
    private final CalorieCalculatorService calorieCalculator;
    
    @Transactional
    public ActivityResponse createActivity(CreateActivityRequest request) {
        log.info("Creating activity for user: {}", request.getUserId());
        
        Activity activity = new Activity();
        activity.setUserId(request.getUserId());
        activity.setName(request.getName());
        activity.setDescription(request.getDescription());
        activity.setActivityType(request.getActivityType());
        activity.setDurationMinutes(request.getDurationMinutes());
        activity.setDistanceKm(request.getDistanceKm());
        activity.setAverageHeartRate(request.getAverageHeartRate());
        activity.setIntensity(request.getIntensity());
        activity.setNotes(request.getNotes());
        activity.setActivityDate(request.getActivityDate());
        activity.setWorkoutId(request.getWorkoutId());
        
        // Calculate calories if not provided
        if (request.getCaloriesBurned() == null) {
            int estimatedCalories = calorieCalculator.calculateCalories(
                    request.getActivityType(),
                    request.getDurationMinutes(),
                    request.getIntensity()
            );
            activity.setCaloriesBurned(estimatedCalories);
        } else {
            activity.setCaloriesBurned(request.getCaloriesBurned());
        }
        
        Activity savedActivity = activityRepository.save(activity);
        log.info("Activity created with ID: {}", savedActivity.getId());
        
        // Publish event to RabbitMQ
        eventPublisher.publishActivityCreatedEvent(savedActivity);
        
        return mapToActivityResponse(savedActivity);
    }
    
    public ActivityResponse getActivityById(Long activityId) {
        log.info("Fetching activity with ID: {}", activityId);
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID: " + activityId));
        return mapToActivityResponse(activity);
    }
    
    public List<ActivityResponse> getUserActivities(String userId) {
        log.info("Fetching all activities for user: {}", userId);
        return activityRepository.findByUserIdOrderByActivityDateDesc(userId).stream()
                .map(this::mapToActivityResponse)
                .collect(Collectors.toList());
    }
    
    public List<ActivityResponse> getUserActivitiesByDateRange(
            String userId, 
            LocalDateTime startDate, 
            LocalDateTime endDate) {
        log.info("Fetching activities for user {} between {} and {}", userId, startDate, endDate);
        return activityRepository.findByUserIdAndActivityDateBetween(userId, startDate, endDate).stream()
                .map(this::mapToActivityResponse)
                .collect(Collectors.toList());
    }
    
    public ActivityStatsResponse getUserStats(String userId) {
        log.info("Calculating stats for user: {}", userId);
        
        Long totalActivities = activityRepository.countByUserId(userId);
        Integer totalDuration = activityRepository.sumDurationByUserId(userId);
        Integer totalCalories = activityRepository.sumCaloriesByUserId(userId);
        Double totalDistance = activityRepository.sumDistanceByUserId(userId);
        Double avgDuration = activityRepository.avgDurationByUserId(userId);
        
        // Handle nulls
        totalDuration = totalDuration != null ? totalDuration : 0;
        totalCalories = totalCalories != null ? totalCalories : 0;
        totalDistance = totalDistance != null ? totalDistance : 0.0;
        avgDuration = avgDuration != null ? avgDuration : 0.0;
        
        Integer avgCalories = totalActivities > 0 ? totalCalories / totalActivities.intValue() : 0;
        
        return new ActivityStatsResponse(
                userId,
                totalActivities,
                totalDuration,
                totalCalories,
                totalDistance,
                avgDuration,
                avgCalories
        );
    }
    
    @Transactional
    public void deleteActivity(Long activityId) {
        log.info("Deleting activity with ID: {}", activityId);
        if (!activityRepository.existsById(activityId)) {
            throw new ResourceNotFoundException("Activity not found with ID: " + activityId);
        }
        activityRepository.deleteById(activityId);
        log.info("Activity deleted successfully");
    }
    
    private ActivityResponse mapToActivityResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setName(activity.getName());
        response.setDescription(activity.getDescription());
        response.setActivityType(activity.getActivityType());
        response.setDurationMinutes(activity.getDurationMinutes());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setDistanceKm(activity.getDistanceKm());
        response.setAverageHeartRate(activity.getAverageHeartRate());
        response.setIntensity(activity.getIntensity());
        response.setNotes(activity.getNotes());
        response.setActivityDate(activity.getActivityDate());
        response.setWorkoutId(activity.getWorkoutId());
        response.setCreatedAt(activity.getCreatedAt());
        return response;
    }
}
