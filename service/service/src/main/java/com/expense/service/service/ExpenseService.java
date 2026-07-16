package com.expense.service.service;


import com.expense.service.dto.ExpenseDTO;
import com.expense.service.entities.Expense;
import com.expense.service.repository.ExpenseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

//service always interact with repositories `
@Service
public class ExpenseService
{

    private final ExpenseRepository expenseRepository;// as service and repository talks to eaxh other'

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ExpenseService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }
    // it will hit the endpoint`
    // create epense is when user want to create its expense in app of own not by sms or email, so we will get the expense dto object from controller and then we will convert it to entity and then save it in database using repository
    public boolean createExpense(ExpenseDTO expenseDto){
        setCurrency(expenseDto);
        try{
            expenseRepository.save(objectMapper.convertValue(expenseDto, Expense.class));// as we have to first convert expense dto to entiti we use convertValue()method Expense.class in entities is the class type to which we want to convert the dto object, it will return the converted object and then we save it in database using save() method of repository

            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public boolean updateExpense(ExpenseDTO expenseDto){
        setCurrency(expenseDto);// this is for setting the currency if llm could not detect any currency from the expense description and amount, so we will set it to inr as default currency

        // optional to avoid null pointer exception if we try to find an expense with user id and external id which does not exist in database, so it will return an empty optional object and we can check if it is empty or not before accessing the expense object
        // here Expense is in the repository  wala `
        Optional<Expense> expenseFoundOpt = expenseRepository.findByUserIdAndExternalId(expenseDto.getUserId(), expenseDto.getExternalId());// this is for finding the expense with user id and external id which we want to update, if it is not found then it will return an empty optional object and if it is found then it will return an optional object with the expense object
        if(expenseFoundOpt.isEmpty()){
            return false;
        }
        Expense expense = expenseFoundOpt.get();// to get the expense object from optional object
        expense.setMerchant(Strings.isNotBlank(expenseDto.getMerchant())?expenseDto.getMerchant():expense.getMerchant());// this is for setting the merchant if llm could not detect any merchant from the expense description and amount, so we will set it to null as default merchant
        expense.setCurrency(Strings.isNotBlank(expenseDto.getCurrency())?expenseDto.getMerchant():expense.getCurrency());// this is for setting the currency if llm could not detect any currency from the expense description and amount, so we will set it to inr as default currency
        expenseRepository.save(expense);// save fucnction is already there in jpa repository
        return true;
    }

    public List<ExpenseDTO> getExpenses(String userId){// we used list as one user can have multiple expenses, so we will return a list of expense dto objects, we will get the list of expense entities from database using repository and then we will convert it to list of expense dto objects using object mapper and then return it to controller
        List<Expense> expenseOpt = expenseRepository.findByUserId(userId);
        return objectMapper.convertValue(expenseOpt, new TypeReference<List<ExpenseDTO>>() {});// to convert list of expense entities to list of expense dto objects we use convertValue() method of object mapper and we pass the list of expense entities and the type reference of list of expense dto objects, it will return the converted list of expense dto objects
    // we returntd the dto objects to controller because we don't want to expose the entity objects to controller as it may contain some sensitive information and also it is not good practice to expose entity objects to controller, so we use dto objects to transfer data between controller and service layer
    }

    private void setCurrency(ExpenseDTO expenseDto){
        if(Objects.isNull(expenseDto.getCurrency())){// to chek if llm could not detect any curreny return inr as default currency
            expenseDto.setCurrency("inr");
        }
    }


}