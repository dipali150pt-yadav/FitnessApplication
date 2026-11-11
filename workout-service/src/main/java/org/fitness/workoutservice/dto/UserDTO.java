package org.fitness.workoutservice.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Integer age;
    private Double weight;
    private Double height;
    private String gender;
    private String fitnessLevel;
    private String fitnessGoal;
}
