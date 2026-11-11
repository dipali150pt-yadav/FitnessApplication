package org.fitness.activityservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fitness.activityservice.model.Activity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityEventPublisher {
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${rabbitmq.exchange.name:fitness-exchange}")
    private String exchangeName;
    
    @Value("${rabbitmq.routing-key.activity:activity.created}")
    private String routingKey;
    
    public void publishActivityCreatedEvent(Activity activity) {
        try {
            ActivityEvent event = new ActivityEvent(
                    activity.getId(),
                    activity.getUserId(),
                    activity.getName(),
                    activity.getActivityType(),
                    activity.getDurationMinutes(),
                    activity.getCaloriesBurned(),
                    activity.getActivityDate(),
                    LocalDateTime.now()
            );
            
            rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
            log.info("Published activity created event for activity ID: {} to exchange: {}", 
                    activity.getId(), exchangeName);
            
        } catch (Exception e) {
            log.error("Error publishing activity event: {}", e.getMessage(), e);
        }
    }
}
