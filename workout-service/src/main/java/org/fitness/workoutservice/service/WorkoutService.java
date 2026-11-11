package org.fitness.workoutservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fitness.workoutservice.client.UserServiceClient;
import org.fitness.workoutservice.dto.*;
import org.fitness.workoutservice.exception.ResourceNotFoundException;
import org.fitness.workoutservice.model.DifficultyLevel;
import org.fitness.workoutservice.model.Workout;
import org.fitness.workoutservice.model.WorkoutType;
import org.fitness.workoutservice.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutService {
    
    private final WorkoutRepository workoutRepository;
    private final GeminiAIService geminiAIService;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public WorkoutResponse generateWorkoutRecommendation(WorkoutRecommendationRequest request) {
        log.info("Generating AI workout recommendation for user: {}", request.getUserId());
        
        // Fetch user details if not provided
        UserDTO user = null;
        try {
            user = userServiceClient.getUserById(request.getUserId());
        } catch (Exception e) {
            log.warn("Could not fetch user details: {}", e.getMessage());
        }
        
        // Build AI prompt
        String prompt = buildAIPrompt(request, user);
        log.info("AI Prompt: {}", prompt);
        
        // Get AI recommendation
        String aiResponse = geminiAIService.generateWorkoutRecommendation(prompt);
        log.info("AI Response received");
        
        // Parse and save workout
        Workout workout = parseAIResponse(aiResponse, request.getUserId(), prompt);
        Workout savedWorkout = workoutRepository.save(workout);
        
        log.info("AI-generated workout saved with ID: {}", savedWorkout.getId());
        return mapToWorkoutResponse(savedWorkout);
    }
    
    @Transactional
    public WorkoutResponse createWorkout(CreateWorkoutRequest request) {
        log.info("Creating manual workout for user: {}", request.getUserId());
        
        Workout workout = new Workout();
        workout.setUserId(request.getUserId());
        workout.setName(request.getName());
        workout.setDescription(request.getDescription());
        workout.setType(request.getType());
        workout.setDifficulty(request.getDifficulty());
        workout.setDurationMinutes(request.getDurationMinutes());
        workout.setEstimatedCalories(request.getEstimatedCalories());
        workout.setExercises(request.getExercises());
        workout.setAiGenerated(false);
        
        Workout savedWorkout = workoutRepository.save(workout);
        log.info("Manual workout created with ID: {}", savedWorkout.getId());
        
        return mapToWorkoutResponse(savedWorkout);
    }
    
    public WorkoutResponse getWorkoutById(String workoutId) {
        log.info("Fetching workout with ID: {}", workoutId);
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with ID: " + workoutId));
        return mapToWorkoutResponse(workout);
    }
    
    public List<WorkoutResponse> getUserWorkouts(String userId) {
        log.info("Fetching workouts for user: {}", userId);
        return workoutRepository.findByUserId(userId).stream()
                .map(this::mapToWorkoutResponse)
                .collect(Collectors.toList());
    }
    
    public List<WorkoutResponse> getAIGeneratedWorkouts(String userId) {
        log.info("Fetching AI-generated workouts for user: {}", userId);
        return workoutRepository.findByUserIdAndAiGenerated(userId, true).stream()
                .map(this::mapToWorkoutResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteWorkout(String workoutId) {
        log.info("Deleting workout with ID: {}", workoutId);
        if (!workoutRepository.existsById(workoutId)) {
            throw new ResourceNotFoundException("Workout not found with ID: " + workoutId);
        }
        workoutRepository.deleteById(workoutId);
        log.info("Workout deleted successfully");
    }
    
    private String buildAIPrompt(WorkoutRecommendationRequest request, UserDTO user) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a personalized workout recommendation with the following details:\n\n");
        
        if (user != null) {
            prompt.append("User Profile:\n");
            prompt.append("- Age: ").append(user.getAge() != null ? user.getAge() : "Not specified").append("\n");
            prompt.append("- Gender: ").append(user.getGender() != null ? user.getGender() : "Not specified").append("\n");
            prompt.append("- Weight: ").append(user.getWeight() != null ? user.getWeight() + " kg" : "Not specified").append("\n");
            prompt.append("- Height: ").append(user.getHeight() != null ? user.getHeight() + " cm" : "Not specified").append("\n");
            prompt.append("- Fitness Level: ").append(user.getFitnessLevel() != null ? user.getFitnessLevel() : request.getFitnessLevel()).append("\n");
            prompt.append("- Fitness Goal: ").append(user.getFitnessGoal() != null ? user.getFitnessGoal() : request.getFitnessGoal()).append("\n\n");
        }
        
        prompt.append("Requirements:\n");
        if (request.getPreferredWorkoutType() != null) {
            prompt.append("- Workout Type: ").append(request.getPreferredWorkoutType()).append("\n");
        }
        if (request.getDifficulty() != null) {
            prompt.append("- Difficulty: ").append(request.getDifficulty()).append("\n");
        }
        if (request.getDurationMinutes() != null) {
            prompt.append("- Duration: ").append(request.getDurationMinutes()).append(" minutes\n");
        }
        if (request.getEquipment() != null) {
            prompt.append("- Available Equipment: ").append(request.getEquipment()).append("\n");
        }
        if (request.getHealthConditions() != null) {
            prompt.append("- Health Considerations: ").append(request.getHealthConditions()).append("\n");
        }
        
        prompt.append("\nPlease provide a workout plan in JSON format with the following structure:\n");
        prompt.append("{\n");
        prompt.append("  \"name\": \"Workout Name\",\n");
        prompt.append("  \"description\": \"Brief description\",\n");
        prompt.append("  \"type\": \"CARDIO/STRENGTH/FLEXIBILITY/HIIT/YOGA/PILATES/CROSSFIT/SPORTS/MIXED\",\n");
        prompt.append("  \"difficulty\": \"BEGINNER/INTERMEDIATE/ADVANCED/EXPERT\",\n");
        prompt.append("  \"durationMinutes\": 30,\n");
        prompt.append("  \"estimatedCalories\": 250,\n");
        prompt.append("  \"exercises\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"name\": \"Exercise name\",\n");
        prompt.append("      \"sets\": 3,\n");
        prompt.append("      \"reps\": 12,\n");
        prompt.append("      \"duration\": \"Optional duration\",\n");
        prompt.append("      \"instructions\": \"How to perform\"\n");
        prompt.append("    }\n");
        prompt.append("  ]\n");
        prompt.append("}\n");
        
        return prompt.toString();
    }
    
    private Workout parseAIResponse(String aiResponse, String userId, String prompt) {
        try {
            // Try to extract JSON from the response
            String jsonString = extractJSON(aiResponse);
            JsonNode root = objectMapper.readTree(jsonString);
            
            Workout workout = new Workout();
            workout.setUserId(userId);
            workout.setName(root.path("name").asText("AI Generated Workout"));
            workout.setDescription(root.path("description").asText(""));
            
            String typeStr = root.path("type").asText("MIXED");
            workout.setType(WorkoutType.valueOf(typeStr));
            
            String difficultyStr = root.path("difficulty").asText("BEGINNER");
            workout.setDifficulty(DifficultyLevel.valueOf(difficultyStr));
            
            workout.setDurationMinutes(root.path("durationMinutes").asInt(30));
            workout.setEstimatedCalories(root.path("estimatedCalories").asInt(200));
            workout.setExercises(objectMapper.writeValueAsString(root.path("exercises")));
            workout.setAiGenerated(true);
            workout.setAiPrompt(prompt);
            
            return workout;
            
        } catch (Exception e) {
            log.error("Error parsing AI response: {}", e.getMessage());
            
            // Return a default workout if parsing fails
            Workout workout = new Workout();
            workout.setUserId(userId);
            workout.setName("AI Generated Workout");
            workout.setDescription("A general fitness workout");
            workout.setType(WorkoutType.MIXED);
            workout.setDifficulty(DifficultyLevel.BEGINNER);
            workout.setDurationMinutes(30);
            workout.setEstimatedCalories(200);
            workout.setExercises(aiResponse);
            workout.setAiGenerated(true);
            workout.setAiPrompt(prompt);
            
            return workout;
        }
    }
    
    private String extractJSON(String text) {
        // Try to find JSON object in the text
        int startIndex = text.indexOf("{");
        int endIndex = text.lastIndexOf("}");
        
        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return text.substring(startIndex, endIndex + 1);
        }
        
        return text;
    }
    
    private WorkoutResponse mapToWorkoutResponse(Workout workout) {
        WorkoutResponse response = new WorkoutResponse();
        response.setId(workout.getId());
        response.setUserId(workout.getUserId());
        response.setName(workout.getName());
        response.setDescription(workout.getDescription());
        response.setType(workout.getType());
        response.setDifficulty(workout.getDifficulty());
        response.setDurationMinutes(workout.getDurationMinutes());
        response.setEstimatedCalories(workout.getEstimatedCalories());
        response.setExercises(workout.getExercises());
        response.setAiGenerated(workout.getAiGenerated());
        response.setCreatedAt(workout.getCreatedAt());
        return response;
    }
}
