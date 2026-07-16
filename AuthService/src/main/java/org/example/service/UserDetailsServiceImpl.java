package org.example.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entities.UserInfo;
import org.example.entities.UserRole;
import org.example.eventProducer.UserInfoEvent;
import org.example.eventProducer.UserInfoProducer;
import org.example.model.UserInfoDto;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserInfoProducer userInfoProducer;


    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Entering in loadUserByUsername Method...");
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("Username not found: " + username);
            throw new UsernameNotFoundException("could not found user..!!");
        }
        log.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user);
    }
    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto) {
        return userRepository.findByUsername(userInfoDto.getUsername());// this for checking if user already exists or not .getUsername() is used to get the username from userInfoDto object
    }
    public Boolean signupUser(UserInfoDto userInfoDto){
        //        ValidationUtil.validateUserAttributes(userInfoDto);
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if(Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))){
            return false;
        }
        String userId = UUID.randomUUID().toString();// to generate unique user id for each user if user already exists then it will return false and if not then it will generate unique user id and save the user in database
        // Use a typed HashSet so the constructor parameter Set<UserRole> is matched correctly
        userRepository.save(new UserInfo(userId, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<UserRole>())); // save in database we made the userId as unique id for each user and we are saving the user in database with the help of userRepository which is a JPA repository and it will save the user in database and it will return the saved user object


        // pushEventToQueue
        userInfoProducer.sendEventToKafka(userInfoEventToPublish(userInfoDto, userId));
         return true;
     }
    private UserInfoEvent userInfoEventToPublish(UserInfoDto userInfoDto, String userId){// we are givig user id to userservice to tell consume this event and then we are creating userInfoEvent object with the help of userInfoDto object and userId and then we are returning the userInfoEvent object
        return UserInfoEvent.builder()// builder helps to chain the methods and it will return the userInfoEvent object
                .userId(userId)// these all are chain methods which are used to set the values of userInfoEvent object and then it will return the userInfoEvent object
                .firstName(userInfoDto.getUsername())
                .lastName(userInfoDto.getLastName())// we can use this direltly as we have done getters and setterns on userInfoDto class and we can use this to get the values of userInfoDto object and set the values of userInfoEvent object
                .email(userInfoDto.getEmail())
                .phoneNumber(userInfoDto.getPhoneNumber()).build();

    }
    // note: now we have to serialize the userInfoEvent object and not userinfodto class and then we have to send it to kafka topic and then we have to consume it in userservice and then we have to save the user information in database and then we have to return the response to the client
    //as password should be in authentication service and not in user service so we are not sending the password to kafka topic and we are only sending the user information to kafka topic and then we are consuming it in userservice and then we are saving the user information in database and then we are returning the response to the client so therefore we are sending userInfoEvent insted of userInfoDto to kafka topic and then we are consuming it in userservice and then we are saving the user information in database and then we are returning the response to the client
 }

