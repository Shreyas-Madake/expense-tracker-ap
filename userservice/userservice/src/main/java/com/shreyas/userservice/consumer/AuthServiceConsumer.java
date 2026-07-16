package com.shreyas.userservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shreyas.userservice.entities.UserInfo;
import com.shreyas.userservice.entities.UserInfoDto;
import com.shreyas.userservice.repository.UserRepositoy;
import com.shreyas.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceConsumer { // this class consumes kafka events sent by the auth service and stores user info in the DB

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @KafkaListener(topics = "${spring.kafka.topic-json.name}",groupId = "${spring.kafka.consumer.group-id}")// this is for listening to the kafka topic for the user information sent by the auth service and then storing the user information in the database
    public void listen(UserInfoDto eventData) {
        try {
            // Todo: Make it transactional, to handle idempotency and validate email, phoneNumber etc
            userService.createOrUpdateUser(eventData);
        } catch (Exception ex) {

            {
                ex.printStackTrace();
                System.out.println("AuthServiceConsumer: Exception is thrown while consuming kafka event");

            }
        }

    }
}