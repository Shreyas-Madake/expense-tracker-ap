package com.expense.service.consumer;

import com.expense.service.dto.ExpenseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ExpenseDeserializer implements Deserializer<ExpenseDTO>// we have to deseriale it in expensedto
{

    // we have to override some methods and we dont have to give its implementation  in desealiser

    @Override public void close() {
    }
    @Override public void configure(Map<String, ?> arg0, boolean arg1) {
    }

    @Override// main method(to override for deserializing the byte array to ExpenseDTO object)
    public ExpenseDTO deserialize(String arg0, byte[] arg1) {
        ObjectMapper mapper = new ObjectMapper();
        ExpenseDTO expense = null;
        try {
            expense = mapper.readValue(arg1, ExpenseDTO.class);// this is for converting the byte array to ExpenseDTO object using the object mapper
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expense;
    }


}
