package org.fitness.activityservice.service;

import lombok.extern.slf4j.Slf4j;
import org.fitness.activityservice.model.ActivityType;
import org.fitness.activityservice.model.IntensityLevel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CalorieCalculatorService {
    
    // MET (Metabolic Equivalent of Task) values for different activities
    private static final Map<ActivityType, Double> BASE_MET_VALUES = new HashMap<>();
    
    static {
        BASE_MET_VALUES.put(ActivityType.RUNNING, 9.0);
        BASE_MET_VALUES.put(ActivityType.WALKING, 3.5);
        BASE_MET_VALUES.put(ActivityType.CYCLING, 7.5);
        BASE_MET_VALUES.put(ActivityType.SWIMMING, 8.0);
        BASE_MET_VALUES.put(ActivityType.WEIGHTLIFTING, 6.0);
        BASE_MET_VALUES.put(ActivityType.YOGA, 3.0);
        BASE_MET_VALUES.put(ActivityType.PILATES, 4.0);
        BASE_MET_VALUES.put(ActivityType.HIKING, 6.5);
        BASE_MET_VALUES.put(ActivityType.DANCING, 5.0);
        BASE_MET_VALUES.put(ActivityType.BASKETBALL, 8.0);
        BASE_MET_VALUES.put(ActivityType.FOOTBALL, 8.5);
        BASE_MET_VALUES.put(ActivityType.TENNIS, 7.0);
        BASE_MET_VALUES.put(ActivityType.BOXING, 9.0);
        BASE_MET_VALUES.put(ActivityType.ROWING, 7.0);
        BASE_MET_VALUES.put(ActivityType.CLIMBING, 8.5);
        BASE_MET_VALUES.put(ActivityType.AEROBICS, 6.5);
        BASE_MET_VALUES.put(ActivityType.STRETCHING, 2.5);
        BASE_MET_VALUES.put(ActivityType.OTHER, 5.0);
    }
    
    /**
     * Calculate calories burned based on activity type, duration, and intensity
     * Formula: Calories = MET * weight(kg) * time(hours)
     * Using average weight of 70kg for estimation
     */
    public int calculateCalories(ActivityType activityType, Integer durationMinutes, IntensityLevel intensity) {
        if (activityType == null || durationMinutes == null) {
            log.warn("Invalid input for calorie calculation");
            return 0;
        }
        
        double baseMET = BASE_MET_VALUES.getOrDefault(activityType, 5.0);
        double adjustedMET = adjustMetForIntensity(baseMET, intensity);
        double averageWeight = 70.0; // kg
        double hours = durationMinutes / 60.0;
        
        int calories = (int) Math.round(adjustedMET * averageWeight * hours);
        
        log.debug("Calculated {} calories for {} activity of {} minutes at {} intensity", 
                calories, activityType, durationMinutes, intensity);
        
        return calories;
    }
    
    private double adjustMetForIntensity(double baseMET, IntensityLevel intensity) {
        if (intensity == null) {
            return baseMET;
        }
        
        return switch (intensity) {
            case LOW -> baseMET * 0.7;
            case MODERATE -> baseMET;
            case HIGH -> baseMET * 1.3;
            case EXTREME -> baseMET * 1.6;
        };
    }
}
