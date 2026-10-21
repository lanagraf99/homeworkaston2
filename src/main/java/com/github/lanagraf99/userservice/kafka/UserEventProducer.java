package com.github.lanagraf99.userservice.kafka;

import com.github.lanagraf99.userservice.dto.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    private static final String TOPIC = "user-events";
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserEvent(UserEvent event) {
        kafkaTemplate.send(TOPIC, event);
        System.out.println("Отправлено событие в Kafka: " + event.getOperation() + " для " + event.getEmail());
    }
}
