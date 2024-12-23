package com.nttdata.credit_card.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ExpenseRequest {

    private String clientId;
    private Double amount;
    private String description;

    public ExpenseRequest(String clientId, Double amount, String description) {
        setClientId(clientId);
        setAmount(amount);
        setDescription(description);
    }

    public void setClientId(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("Client ID is required");
        }
        this.clientId = clientId;
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
        this.description = description;
    }
}