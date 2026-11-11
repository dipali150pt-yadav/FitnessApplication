package org.fitness.userservice.dto;

import lombok.Data;
import org.fitness.userservice.model.FitnessLevel;
import org.fitness.userservice.model.Gender;
import org.fitness.userservice.model.UserRole;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private Integer age;
    private Double weight;
    private Double height;
    private Gender gender;
    private FitnessLevel fitnessLevel;
    private String fitnessGoal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
