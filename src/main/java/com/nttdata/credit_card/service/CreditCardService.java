package com.nttdata.credit_card.service;

import com.nttdata.credit_card.model.request.CreditCardRequest;
import com.nttdata.credit_card.model.request.ExpenseRequest;
import com.nttdata.credit_card.model.response.BalanceResponse;
import com.nttdata.credit_card.model.response.CreditCardResponse;
import com.nttdata.credit_card.model.response.ExpenseResponse;
import com.nttdata.credit_card.model.response.TransactionCreditCardResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService {
    Flux<CreditCardResponse> getAllCreditCards();
    Mono<CreditCardResponse> getCreditCardById(String idCredit);
    Mono<CreditCardResponse> createNewCreditCard(CreditCardRequest creditCardRequest);
    Mono<CreditCardResponse> updateCreditCard(String id, CreditCardRequest creditCardRequest);
    Mono<Void> deleteCreditCard(String id);
    Mono<ExpenseResponse>chargeByCardId(String id, ExpenseRequest expenseRequest);
    Mono<ExpenseResponse>paymentByCardId(String id, ExpenseRequest expenseRequest);
    Flux<BalanceResponse> getBalanceByClientId(String idClient);
    Mono<TransactionCreditCardResponse>getTransactionByCreditCard(String idAccount);
    Flux<CreditCardResponse>getAllCreditCardByClientId(String clientId);
}
