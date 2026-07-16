package com.shreyas.userservice.services;

import com.shreyas.userservice.entities.UserInfo;
import com.shreyas.userservice.entities.UserInfoDto;
import com.shreyas.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;// as service class is annotated with @Service, it is a Spring component and its instance will be managed by the Spring container. The @RequiredArgsConstructor annotation generates a constructor that takes all final fields as parameters, which allows for dependency injection of the UserRepository instance.

    public UserInfoDto createOrUpdateUser(UserInfoDto userInfoDto){
        UnaryOperator<UserInfo> updatingUser = user -> {// it is a functional interface that takes a UserInfo object and returns a UserInfo object. It is used to update the user if it already exists in the database
            // this is for updating the user if it already exists in the database
            UserInfo transformed = userInfoDto.transformToUserInfo();
            transformed.setId(user.getId()); // preserve existing id when updating
            return userRepository.save(transformed);//this will save the updated user in the database and return the updated user object
        };

        Supplier<UserInfo> createUser = () -> {
            return userRepository.save(userInfoDto.transformToUserInfo());
        };

        UserInfo userInfo = userRepository.findByUserId(userInfoDto.getUserId())
                .map(updatingUser)//mapis used to apply the updatingUser function to the user object if it is present in the database and return the updated user object
                .orElseGet(createUser);
        return new UserInfoDto(
                userInfo.getUserId(),
                userInfo.getFirstName(),
                userInfo.getLastName(),
                userInfo.getPhoneNumber(),
                userInfo.getEmail(),
                userInfo.getProfilePic()
        );
    }

    public UserInfoDto getUser(UserInfoDto userInfoDto) throws Exception{
        Optional<UserInfo> userInfoDtoOpt = userRepository.findByUserId(userInfoDto.getUserId());
        if(userInfoDtoOpt.isEmpty()){
            throw new Exception("User not found");
        }
        UserInfo userInfo = userInfoDtoOpt.get();
        return new UserInfoDto(
                userInfo.getUserId(),
                userInfo.getFirstName(),
                userInfo.getLastName(),
                userInfo.getPhoneNumber(),
                userInfo.getEmail(),
                userInfo.getProfilePic()
        );
    }

}