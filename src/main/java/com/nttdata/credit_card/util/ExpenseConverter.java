package com.nttdata.credit_card.util;

import com.nttdata.credit_card.model.request.ExpenseRequest;
import com.nttdata.credit_card.model.response.ExpenseResponse;

import java.util.Date;

public class ExpenseConverter {
    public static ExpenseResponse convertToResponse(ExpenseRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("ExpenseRequest cannot be null");
        }
        return new ExpenseResponse(
                request.getAmount(),
                new Date()
        );
    }
}
