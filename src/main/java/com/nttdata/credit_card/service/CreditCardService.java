package com.nttdata.credit_card.service;

import com.nttdata.credit_card.model.request.CreditCardRequest;
import com.nttdata.credit_card.model.response.CreditCardResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService {
    Flux<CreditCardResponse> getAllCreditCards();
    Mono<CreditCardResponse> getCreditCardById(String idCredit);
    Mono<CreditCardResponse> createNewCreditCard(CreditCardRequest creditCardRequest);
    Mono<CreditCardResponse> updateCreditCard(String id, CreditCardRequest creditCardRequest);
    Mono<Void> deleteCreditCard(String id);
}
