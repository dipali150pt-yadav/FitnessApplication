package org.fitness.workoutservice.client;

import org.fitness.workoutservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    
    @GetMapping("/users/{userId}")
    UserDTO getUserById(@PathVariable("userId") String userId);
}
