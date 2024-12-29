package com.nttdata.credit_card.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nttdata.credit_card.model.entity.Transaction;
import com.nttdata.credit_card.model.enums.TypeCredit;
import com.nttdata.credit_card.util.CreditTypeDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Getter
@NoArgsConstructor
public class CreditCardRequest {
    @JsonDeserialize(using = CreditTypeDeserializer.class)
    private TypeCredit type;
    private Double creditLimit;
    private Double availableBalance;
    private Date issueDate;
    private Date expirationDate;
    private String clientId;
    private List<Transaction> transactions;

    public CreditCardRequest(TypeCredit type, Double creditLimit, Double availableBalance, Date issueDate, Date expirationDate, String clientId ,List<Transaction> transactions) {
       setType(type);
        setCreditLimit(creditLimit);
        setAvailableBalance(availableBalance);
        setIssueDate(issueDate);
        setExpirationDate(expirationDate);
        setClientId(clientId);
        setTransactions(transactions);
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }


    public void setType(TypeCredit type) {
        if (type == null) {
            throw new IllegalArgumentException("Type is required");
        }
        this.type = type;
    }

    public void setCreditLimit(Double creditLimit) {
        if (creditLimit == null || creditLimit <= 0) {
            throw new IllegalArgumentException("Credit limit must be positive");
        }
        this.creditLimit = creditLimit;
    }

    public void setAvailableBalance(Double availableBalance) {
        if (availableBalance == null || availableBalance < 0 || availableBalance==getCreditLimit()) {
            throw new IllegalArgumentException("Available balance must be zero or positive");
        }
        this.availableBalance = availableBalance;
    }

    public void setIssueDate(Date issueDate) {
        if (issueDate == null || issueDate.after(new Date())) {
            throw new IllegalArgumentException("Issue date must be in the past or present");
        }
        this.issueDate = issueDate;
    }

    public void setExpirationDate(Date expirationDate) {
        if (expirationDate == null || !expirationDate.after(new Date())) {
            throw new IllegalArgumentException("Expiration date must be in the future");
        }
        this.expirationDate = expirationDate;
    }

    public void setClientId(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("Client ID is required");
        }
        this.clientId = clientId;
    }
}
