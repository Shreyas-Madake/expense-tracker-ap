package com.shreyas.userservice.repository;


import com.shreyas.userservice.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository// this annotaon makes its instance available for dependency injection in other classes
public interface UserRepositoy extends UserRepository {
    // Deprecated compatibility wrapper: UserRepository is the preferred interface name
}
