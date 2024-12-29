package com.nttdata.credit_card.util;


import com.nttdata.credit_card.model.entity.CreditCard;
import com.nttdata.credit_card.model.entity.Transaction;
import com.nttdata.credit_card.model.enums.TransactionType;
import com.nttdata.credit_card.model.request.ExpenseRequest;
import com.nttdata.credit_card.model.request.TransactionRequest;
import com.nttdata.credit_card.model.response.TransactionCreditCardResponse;
import com.nttdata.credit_card.model.response.TransactionResponse;
import reactor.core.publisher.Mono;

import java.util.Date;

public class TransactionConverter {
    public static Mono<Transaction> toTransaction(TransactionRequest transactionRequest, String clientId, TransactionType type, String description) {
        if (transactionRequest == null) {
            return Mono.error(new IllegalArgumentException("TransactionRequest is null"));
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setType(type);
        transaction.setDate(new Date());
        transaction.setDescription(description);
        return Mono.just(transaction);
    }

    public static Mono<TransactionResponse> toTransactionResponse(Transaction transaction) {
        if (transaction == null) {
            return Mono.error(new IllegalArgumentException("Transaction is null"));
        }
        TransactionResponse transactionResponse = new TransactionResponse(
                transaction.getType(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getDescription()
        );
        return Mono.just(transactionResponse);
    }
    public static Mono<Transaction>toTransactionExpense(ExpenseRequest expenseRequest,TransactionType transactionType,String description){
        if (expenseRequest == null) {
            return Mono.error(new IllegalArgumentException("expenseRequest is null"));
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(expenseRequest.getAmount());
        transaction.setType(transactionType);
        transaction.setDescription(description);
        transaction.setDate(new Date());
        return Mono.just(transaction);
    }
    public static TransactionCreditCardResponse toTransactionAccountResponse(CreditCard creditCard) {
        TransactionCreditCardResponse transactionResponse = new TransactionCreditCardResponse();
        transactionResponse.setTransactions(creditCard.getTransactions());
        return transactionResponse;
    }
}
