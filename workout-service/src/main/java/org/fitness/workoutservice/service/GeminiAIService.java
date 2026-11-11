package org.fitness.workoutservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiAIService {
    
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;
    
    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Value("${gemini.api.url}")
    private String apiUrl;
    
    public String generateWorkoutRecommendation(String prompt) {
        log.info("Generating AI workout recommendation");
        
        try {
            Map<String, Object> requestBody = buildRequestBody(prompt);
            
            WebClient webClient = webClientBuilder.build();
            
            String response = webClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(error -> {
                        log.error("Error calling Gemini API: {}", error.getMessage());
                        return Mono.just(getDefaultWorkoutRecommendation());
                    })
                    .block();
            
            return extractTextFromResponse(response);
            
        } catch (Exception e) {
            log.error("Exception in AI service: {}", e.getMessage(), e);
            return getDefaultWorkoutRecommendation();
        }
    }
    
    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        
        Map<String, Object> content = new HashMap<>();
        Map<String, String> part = new HashMap<>();
        part.put("text", prompt);
        
        content.put("parts", List.of(part));
        requestBody.put("contents", List.of(content));
        
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("maxOutputTokens", 2048);
        requestBody.put("generationConfig", generationConfig);
        
        return requestBody;
    }
    
    private String extractTextFromResponse(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode candidates = root.path("candidates");
            
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode content = candidates.get(0).path("content");
                JsonNode parts = content.path("parts");
                
                if (parts.isArray() && parts.size() > 0) {
                    String text = parts.get(0).path("text").asText();
                    log.info("Successfully extracted AI recommendation");
                    return text;
                }
            }
            
            log.warn("Could not extract text from Gemini response, using default");
            return getDefaultWorkoutRecommendation();
            
        } catch (Exception e) {
            log.error("Error parsing Gemini response: {}", e.getMessage());
            return getDefaultWorkoutRecommendation();
        }
    }
    
    private String getDefaultWorkoutRecommendation() {
        return """
                {
                    "name": "Full Body Beginner Workout",
                    "description": "A balanced workout routine suitable for beginners",
                    "type": "MIXED",
                    "difficulty": "BEGINNER",
                    "durationMinutes": 30,
                    "estimatedCalories": 200,
                    "exercises": [
                        {
                            "name": "Jumping Jacks",
                            "sets": 3,
                            "reps": 15,
                            "duration": "5 minutes",
                            "instructions": "Classic cardio warm-up exercise"
                        },
                        {
                            "name": "Bodyweight Squats",
                            "sets": 3,
                            "reps": 12,
                            "instructions": "Keep your back straight and lower until thighs are parallel to ground"
                        },
                        {
                            "name": "Push-ups",
                            "sets": 3,
                            "reps": 10,
                            "instructions": "Modified push-ups on knees if needed"
                        },
                        {
                            "name": "Plank",
                            "sets": 3,
                            "duration": "30 seconds",
                            "instructions": "Hold a straight body position"
                        },
                        {
                            "name": "Lunges",
                            "sets": 3,
                            "reps": 10,
                            "instructions": "Alternate legs, keep front knee at 90 degrees"
                        }
                    ]
                }
                """;
    }
}
