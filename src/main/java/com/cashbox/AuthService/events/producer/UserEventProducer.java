package com.cashbox.AuthService.events.producer;


import com.cashbox.AuthService.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;


    public void publishUserRegistered(UserRegisteredEvent event) {
        kafkaTemplate.send("user-registered-topic", event.getUserId().toString(), event);
        System.out.println("✅Produced event: " + event);
    }
}
