package com.expense.service.consumer;
// after deserialize it should come in consumer and then we will save it in database
import com.expense.service.dto.ExpenseDTO;
import com.expense.service.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
// this class is for consuming the kafka event from the topic and then we will save it in database, so we will use the expense service to save it in database, so we will inject the expense service here
@RequiredArgsConstructor
@Service// it means this class is a service component in the Spring framework, and it will be automatically detected and registered as a bean in the application context. It is used to indicate that this class contains business logic and can be injected into other components.
public class ExpenseConsumer
{

    private ExpenseService expenseService;// spring will automatically injetc its instance here

    @Autowired
    ExpenseConsumer(ExpenseService expenseService){// componetn in this will be autowied
        this.expenseService = expenseService;
    }

    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")// take from application.properties, this is for consuming the kafka event from the topic, it will listen to the topic and when it receives the event it will call the listen method and pass the event data to it
    public void listen(ExpenseDTO eventData) {// expencedto will come deserialized from the kafka topic and then we will save it in database
        try{
            // Todo: Make it transactional, and check if duplicate event (Handle idempotency)
            expenseService.createExpense(eventData);
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("AuthServiceConsumer: Exception is thrown while consuming kafka event");
        }
    }

}

// JUST FOR KNOWLEDGE
// fetch from SQL--> Business Logic--> send to Kafka--> Consume from Kafka--> Save in database
// SQL is atomic when its saving it will not allow to read it
// fetch from SQL(by userid,externalid)-->Business logic-->save into SQL(expenseRepository.save)