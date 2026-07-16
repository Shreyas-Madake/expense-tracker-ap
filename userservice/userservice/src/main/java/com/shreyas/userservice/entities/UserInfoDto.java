package com.shreyas.userservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.NonNull;



@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // this is for converting the camel case to snake case while sending the user information to the kafka topic
@Data
@AllArgsConstructor
@NoArgsConstructor// this is for creating the constructor for the UserInfoDto class
@Builder// this is for creating the builder pattern for the UserInfoDto class
@JsonIgnoreProperties(ignoreUnknown = true) // this is for ignoring the unknown properties while deserializing the user information from the kafka topic
public class UserInfoDto {

    @JsonProperty("user_id")// to tell the json property name to be user_id instead of userId while sending the user information to the kafka topic and also while deserializing the user information from the kafka topic
    @NonNull
    private String userId;

    @JsonProperty("first_name")
    @NonNull
    private String firstName;

    @JsonProperty("last_name")
    @NonNull
    private String lastName;

    @JsonProperty("phone_number")
    @NonNull
    private Long phoneNumber;

    @JsonProperty("email")
    @NonNull
    private String email;

    @JsonProperty("profile_pic")
    private String profilePic;

    public UserInfo transformToUserInfo() {
        return UserInfo.builder()
                .firstName(firstName)// we didnt use id as it is auto generated in the database while creating the user information in the database
                .lastName(lastName)
                .userId(userId)
                .email(email)
                .profilePic(profilePic)
                .phoneNumber(phoneNumber).build();
    }

}
