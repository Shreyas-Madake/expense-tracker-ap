package com.expense.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.security.Timestamp;

// dto(Data Transfer Object) is for transferring data between different layers of the application, such as from the service layer to the controller layer or from the controller layer to the client. It is used to encapsulate the data and provide a structured format for communication between different components of the application. The ExpenseDTO class can contain fields that represent the properties of an expense, such as amount, description, date, etc., and can be used to transfer this data between different parts of the application.
@AllArgsConstructor// this we write everytime
@NoArgsConstructor//this we write evrytime
// this for creating a constructor with all arguments and a constructor with no arguments, which is required for some frameworks and libraries that use reflection to create instances of the class. By using these annotations, we can avoid writing boilerplate code for constructors and focus on the actual properties and behavior of the class.
@Builder//to chain things
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)// this is for converting camelCase to snake_case in json response
@JsonIgnoreProperties(ignoreUnknown = true)// if some value is null so ignore that value while deserializing the user information from the kafka topic
public class ExpenseDTO {

    private String externalId;//uuid is used for generating unique id for each expense, which is useful for tracking and referencing expenses in the system. It can be used as a primary key in the database and can also be used to link expenses to other entities, such as users or categories.
    @JsonProperty(value = "amount")// when we use kafka for converting it in real time it cannot convert so we use this to tell this is the amount field in the json response and also while deserializing the expense information from the kafka topic
    private String amount;

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "merchant")
    private String merchant;

    @JsonProperty(value = "currency")
    private String currency;

    @JsonProperty(value = "created_at")
    private Timestamp createdAt;


    public ExpenseDTO(String json) {// this is to deserialize the expense information from the kafka topic, which is in json format, and convert it into an ExpenseDTO object. This constructor takes a json string as input and uses the ObjectMapper class from the Jackson library to parse the json and populate the fields of the ExpenseDTO object accordingly. The @JsonProperty annotations are used to map the json properties to the corresponding fields in the ExpenseDTO class, ensuring that the deserialization process works correctly even if the json property names differ from the field names in the class.
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            ExpenseDTO expense = mapper.readValue(json, ExpenseDTO.class);
            this.externalId = expense.externalId;
            this.amount = expense.amount;
            this.userId = expense.userId;
            this.merchant = expense.merchant;
            this.currency = expense.currency;
            this.createdAt = expense.createdAt;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize ExpenseDto from JSON", e);
        }
    }
}
