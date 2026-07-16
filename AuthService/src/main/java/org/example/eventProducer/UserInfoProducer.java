package org.example.eventProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Service
public class UserInfoProducer {
    private final KafkaTemplate<String, UserInfoEvent> kafkaTemplate;// this is for sending messages to Kafka, it is injected by Spring

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;// this is the name of the Kafka topic, it is injected from application.properties

    @Autowired
    UserInfoProducer(KafkaTemplate<String, UserInfoEvent> kafkaTemplate) {// key and value is from auth service, it is injected by Spring
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendEventToKafka(UserInfoEvent eventData) {

        Message<UserInfoEvent> message= MessageBuilder.withPayload(eventData).setHeader(KafkaHeaders.TOPIC, TOPIC_NAME).build();
        kafkaTemplate.send(message);
    }
}