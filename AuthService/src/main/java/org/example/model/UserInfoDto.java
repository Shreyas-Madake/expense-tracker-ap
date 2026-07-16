package org.example.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.example.entities.UserInfo;

@JsonNaming (PropertyNamingStrategy.SnakeCaseStrategy.class)// this is for converting camelCase to snake_case in json response
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserInfoDto extends UserInfo
{

    @NonNull// it tells his should not be null while creating the user information and also while deserializing the user information from the kafka topic
    private String firstName; // first_name

    @NonNull
    private String lastName; //last_name

    private Long phoneNumber;

    private String email; // email



}