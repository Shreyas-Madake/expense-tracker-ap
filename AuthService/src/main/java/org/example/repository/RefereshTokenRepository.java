package org.example.repository;

import org.example.entities.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefereshTokenRepository extends CrudRepository<RefreshToken, Integer> {// here we are extending CrudRepository to perform CRUD operations on RefreshToken entity. these are query methods that Spring Data JPA will automatically implement based on the method name conventions. By extending CrudRepository, we get access to basic CRUD operations like save, findById, findAll, deleteById, etc. for the RefreshToken entity.
    Optional<RefreshToken> findByToken(String token);// here we are defining a method to find a RefreshToken by its token value, which will return an Optional containing the RefreshToken if found, or empty if not found to handle the case where the token may not exist in the database
// Optional is a container object which may or may not contain a non-null value. It provides methods to check if a value is present, retrieve the value, or provide a default value if the value is not present. This helps to avoid null pointer exceptions and makes the code more robust when dealing with potentially absent values.
}
