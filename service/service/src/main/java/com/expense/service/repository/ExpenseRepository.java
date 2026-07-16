package com.expense.service.repository;

import com.expense.service.entities.Expense;
import org.springframework.data.repository.CrudRepository;

import java.security.Timestamp;
import java.util.List;
import java.util.Optional;
// this is to save the expense data in database and perform the CRUD operations on it
public interface ExpenseRepository extends CrudRepository<Expense, Long> {// it will work as a repository for the Expense entity and provide basic CRUD operations

    List<Expense> findByUserId(String userId);

    List<Expense> findByUserIdAndCreatedAtBetween(String userId, Timestamp startTime, Timestamp endTime);// to find the expenses of a user between a specific time range

    Optional<Expense> findByUserIdAndExternalId(String userId, String externalId);// to find the expense of a user with a specific external id


}