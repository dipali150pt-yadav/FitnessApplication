package org.fitness.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .circuitBreaker(config -> config
                                        .setName("userServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/users")))
                        .uri("lb://user-service"))
                
                // Workout Service Routes
                .route("workout-service", r -> r
                        .path("/api/workouts/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .circuitBreaker(config -> config
                                        .setName("workoutServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/workouts")))
                        .uri("lb://workout-service"))
                
                // Activity Service Routes
                .route("activity-service", r -> r
                        .path("/api/activities/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .circuitBreaker(config -> config
                                        .setName("activityServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/activities")))
                        .uri("lb://activity-service"))
                
                .build();
    }
}
