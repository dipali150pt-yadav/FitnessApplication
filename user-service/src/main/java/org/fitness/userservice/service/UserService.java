package org.fitness.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fitness.userservice.dto.RegisterRequest;
import org.fitness.userservice.dto.UpdateUserRequest;
import org.fitness.userservice.dto.UserResponse;
import org.fitness.userservice.exception.ResourceNotFoundException;
import org.fitness.userservice.exception.UserAlreadyExistsException;
import org.fitness.userservice.model.User;
import org.fitness.userservice.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Transactional
    public UserResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAge(request.getAge());
        user.setWeight(request.getWeight());
        user.setHeight(request.getHeight());
        user.setGender(request.getGender());
        user.setFitnessLevel(request.getFitnessLevel());
        user.setFitnessGoal(request.getFitnessGoal());
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());
        
        return mapToUserResponse(savedUser);
    }
    
    public UserResponse getUserById(String userId) {
        log.info("Fetching user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return mapToUserResponse(user);
    }
    
    public UserResponse getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToUserResponse(user);
    }
    
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getAge() != null) {
            user.setAge(request.getAge());
        }
        if (request.getWeight() != null) {
            user.setWeight(request.getWeight());
        }
        if (request.getHeight() != null) {
            user.setHeight(request.getHeight());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getFitnessLevel() != null) {
            user.setFitnessLevel(request.getFitnessLevel());
        }
        if (request.getFitnessGoal() != null) {
            user.setFitnessGoal(request.getFitnessGoal());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        
        return mapToUserResponse(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        
        userRepository.deleteById(userId);
        log.info("User deleted successfully with ID: {}", userId);
    }
    
    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole());
        response.setAge(user.getAge());
        response.setWeight(user.getWeight());
        response.setHeight(user.getHeight());
        response.setGender(user.getGender());
        response.setFitnessLevel(user.getFitnessLevel());
        response.setFitnessGoal(user.getFitnessGoal());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
