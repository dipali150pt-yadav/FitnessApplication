package org.fitness.activityservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    @Value("${rabbitmq.exchange.name:fitness-exchange}")
    private String exchangeName;
    
    @Value("${rabbitmq.queue.activity:activity-queue}")
    private String activityQueueName;
    
    @Value("${rabbitmq.routing-key.activity:activity.created}")
    private String activityRoutingKey;
    
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }
    
    @Bean
    public Queue activityQueue() {
        return new Queue(activityQueueName, true);
    }
    
    @Bean
    public Binding activityBinding() {
        return BindingBuilder
                .bind(activityQueue())
                .to(exchange())
                .with(activityRoutingKey);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
