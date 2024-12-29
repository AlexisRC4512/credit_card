package com.nttdata.credit_card.util;

import com.nttdata.credit_card.model.entity.CreditCard;
import com.nttdata.credit_card.model.enums.TypeCredit;
import com.nttdata.credit_card.model.request.CreditCardRequest;
import com.nttdata.credit_card.model.response.CreditCardResponse;

public class CreditCardConverter {
    public static CreditCard toCreditCard(CreditCardRequest request) {
        CreditCard creditCard = new CreditCard();
        creditCard.setType(request.getType());
        creditCard.setCreditLimit(request.getCreditLimit());
        creditCard.setAvailableBalance(request.getAvailableBalance());
        creditCard.setIssueDate(request.getIssueDate());
        creditCard.setExpirationDate(request.getExpirationDate());
        creditCard.setClientId(request.getClientId());
        creditCard.setTransactions(request.getTransactions());
        return creditCard;
    }

    public static CreditCardResponse toCreditCardResponse(CreditCard creditCard) {
        CreditCardResponse response = new CreditCardResponse();
        response.setId(creditCard.getId());
        response.setType(TypeCredit.valueOf(creditCard.getType().name()));
        response.setCreditLimit(creditCard.getCreditLimit());
        response.setAvailableBalance(creditCard.getAvailableBalance());
        response.setIssueDate(creditCard.getIssueDate());
        response.setExpirationDate(creditCard.getExpirationDate());
        response.setClientId(creditCard.getClientId());
        response.setTransactions(creditCard.getTransactions());
        return response;
    }
}
