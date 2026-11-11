package org.fitness.userservice.dto;

import lombok.Data;
import org.fitness.userservice.model.FitnessLevel;
import org.fitness.userservice.model.Gender;

@Data
public class UpdateUserRequest {
    
    private String firstName;
    private String lastName;
    private Integer age;
    private Double weight;
    private Double height;
    private Gender gender;
    private FitnessLevel fitnessLevel;
    private String fitnessGoal;
}
