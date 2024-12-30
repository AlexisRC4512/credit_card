package com.nttdata.credit_card.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ExpenseRequest {

    private Double amount;

    public ExpenseRequest(String clientId, Double amount, String description) {
        setAmount(amount);
    }


    public void setAmount(Double amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.amount = amount;
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
    }
}