package org.fitness.activityservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fitness.activityservice.dto.ActivityResponse;
import org.fitness.activityservice.dto.ActivityStatsResponse;
import org.fitness.activityservice.dto.CreateActivityRequest;
import org.fitness.activityservice.service.ActivityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {
    
    private final ActivityService activityService;
    
    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(
            @Valid @RequestBody CreateActivityRequest request) {
        ActivityResponse response = activityService.createActivity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable Long activityId) {
        ActivityResponse response = activityService.getActivityById(activityId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@PathVariable String userId) {
        List<ActivityResponse> activities = activityService.getUserActivities(userId);
        return ResponseEntity.ok(activities);
    }
    
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<ActivityResponse>> getUserActivitiesByDateRange(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ActivityResponse> activities = activityService.getUserActivitiesByDateRange(
                userId, startDate, endDate);
        return ResponseEntity.ok(activities);
    }
    
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<ActivityStatsResponse> getUserStats(@PathVariable String userId) {
        ActivityStatsResponse stats = activityService.getUserStats(userId);
        return ResponseEntity.ok(stats);
    }
    
    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long activityId) {
        activityService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }
}
