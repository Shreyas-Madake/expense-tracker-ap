package org.example.eventProducer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)// if some value is null so ignore that value while deserializing the user information from the kafka topic
@Builder
@Getter
@Setter
@JsonNaming (PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserInfoEvent {
    private String firstName;

    private String lastName;

    private String email;

    private Long phoneNumber;

    private String userId;
}
