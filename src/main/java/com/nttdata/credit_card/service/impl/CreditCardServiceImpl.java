package com.nttdata.credit_card.service.impl;

import com.nttdata.credit_card.model.entity.CreditCard;
import com.nttdata.credit_card.model.entity.Transaction;
import com.nttdata.credit_card.model.enums.TransactionType;
import com.nttdata.credit_card.model.exception.CreditNotFoundException;
import com.nttdata.credit_card.model.exception.InvalidCreditDataException;
import com.nttdata.credit_card.model.request.CreditCardRequest;
import com.nttdata.credit_card.model.request.ExpenseRequest;
import com.nttdata.credit_card.model.response.*;
import com.nttdata.credit_card.service.CreditCardService;
import com.nttdata.credit_card.repository.CreditCardRepository;
import com.nttdata.credit_card.util.BalanceConverter;
import com.nttdata.credit_card.util.CreditCardConverter;
import com.nttdata.credit_card.util.ExpenseConverter;
import com.nttdata.credit_card.util.TransactionConverter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Implementation of the credit card service.
 */
@Service
@Slf4j
public class CreditCardServiceImpl implements CreditCardService {
    @Autowired
    private CreditCardRepository creditCardRepository;

    /**
     * Retrieves all credit cards.
     *
     * @return a flux of credit card responses.
     */
    @Override
    @CircuitBreaker(name = "credit-card", fallbackMethod = "fallbackGetAllCreditCards")
    @TimeLimiter(name = "credit-card")
    public Flux<CreditCardResponse> getAllCreditCards() {
        log.info("Fetching all Credit cards");
        return creditCardRepository.findAll()
                .map(CreditCardConverter::toCreditCardResponse)
                .onErrorMap(e -> new Exception("Error fetching all Credit cards", e));
    }
    /**
     * Retrieves a credit card by its ID.
     *
     * @param idCredit the credit card ID.
     * @return a credit card response.
     */
    @Override
    @CircuitBreaker(name = "credit-card", fallbackMethod = "fallbackGetCreditCardById")
    @TimeLimiter(name = "credit-card")
    public Mono<CreditCardResponse> getCreditCardById(String idCredit) {
        log.debug("Fetching Credit cards with id: {}", idCredit);
        return creditCardRepository.findById(idCredit)
                .map(CreditCardConverter::toCreditCardResponse)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("Credit cards not found with id: " + idCredit)))
                .onErrorMap(e -> new Exception("Error fetching Credit cards by id", e));
    }
    /**
     * Creates a new credit card.
     *
     * @param creditCardRequest the credit card request.
     * @return a credit card response.
     */
    @Override
    @CircuitBreaker(name = "credit-card", fallbackMethod = "fallbackCreateNewCreditCard")
    @TimeLimiter(name = "credit-card")
    public Mono<CreditCardResponse> createNewCreditCard(CreditCardRequest creditCardRequest) {
        if (creditCardRequest == null ) {
            log.warn("Invalid Credit cards data: {}", creditCardRequest);
            return Mono.error(new InvalidCreditDataException("Invalid Credit cards data"));
        }
        Mono<CreditCard> creditCardMono = creditCardRepository.findByNumberCreditCard(creditCardRequest.getNumberCreditCard());
        if (creditCardMono != null) {
            return Mono.error(new InvalidCreditDataException("The Credit Card Number existing"));
        }
        log.info("Creating new Credit cards: {}", creditCardRequest.getType().name());
        CreditCard creditCard = CreditCardConverter.toCreditCard(creditCardRequest);
        return creditCardRepository.save(creditCard)
                .map(CreditCardConverter::toCreditCardResponse)
                .doOnError(e -> log.error("Error creating Credit cards", e))
                .onErrorMap(e -> new Exception("Error creating Credit cards", e));
    }
    /**
     * Updates an existing credit card.
     *
     * @param id                the credit card ID.
     * @param creditCardRequest the credit card request.
     * @return a credit card response.
     */
    @Override
    @CircuitBreaker(name = "credit-card", fallbackMethod = "fallbackUpdateCreditCard")
    @TimeLimiter(name = "credit-card")
    public Mono<CreditCardResponse> updateCreditCard(String id, CreditCardRequest creditCardRequest) {
        if (creditCardRequest == null ) {
            log.warn("Invalid Credit cards data for update: {}", creditCardRequest);
            return Mono.error(new InvalidCreditDataException("Invalid Credit cards data"));
        }
        log.debug("Updating Credit cards with id: {}", id);
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("Credit cards not found with id: " + id)))
                .flatMap(existingClient -> {
                    CreditCard updateCreditCard = CreditCardConverter.toCreditCard(creditCardRequest);
                    updateCreditCard.setId(existingClient.getId());
                    return creditCardRepository.save(updateCreditCard);
                })
                .map(CreditCardConverter::toCreditCardResponse)
                .onErrorMap(e -> new Exception("Error updating Credit cards", e));
    }
    /**
     * Deletes a credit card by its ID.
     *
     * @param id the credit card ID.
     * @return a void Mono.
     */
    @Override
    @CircuitBreaker(name = "credit-card", fallbackMethod = "fallbackDeleteCreditCard")
    @TimeLimiter(name = "credit-card")
    public Mono<Void> deleteCreditCard(String id) {
        log.debug("Deleting Credit cards with id: {}", id);
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("Credit cards not found with id: " + id)))
                .flatMap(existingClient -> creditCardRepository.delete(existingClient))
                .onErrorMap(e -> new Exception("Error deleting Credit cards", e));
    }

    @Override
    @CircuitBreaker(name = "credit-card", fallbackMethod = "fallbackChargeByCardId")
    @TimeLimiter(name = "credit-card")
    public Mono<ExpenseResponse> chargeByCardId(String id, ExpenseRequest expenseRequest) {
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("Credit cards not found with id: " + id)))
                .flatMap(creditCard -> {
                    double newBalance = creditCard.getAvailableBalance() - expenseRequest.getAmount();
                    if (newBalance < 0) {
                        return Mono.error(new IllegalArgumentException("Insufficient available balance"));
                    }
                    Mono<Transaction> transactionMono = TransactionConverter.toTransactionExpense(expenseRequest, TransactionType.PURCHASE, "Charge to credit card");
                    creditCard.setAvailableBalance(newBalance);
                    return updateTransaction(transactionMono, creditCard)
                            .then(creditCardRepository.save(creditCard))
                            .then(Mono.just(ExpenseConverter.convertToResponse(expenseRequest)));
                })
                .doOnError(e -> log.error("Error charging credit card", e))
                .onErrorMap(e -> new Exception("Error charging credit card", e));
    }

    @Override
    @CircuitBreaker(name = "credit-card", fallbackMethod = "fallbackPaymentByCardId")
    @TimeLimiter(name = "credit-card")
    public Mono<ExpenseResponse> paymentByCardId(String id, ExpenseRequest expenseRequest) {
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("Credit cards not found with id: " + id)))
                .flatMap(creditCard -> {
                    double newBalance = creditCard.getAvailableBalance() + expenseRequest.getAmount();
                    if (newBalance > creditCard.getCreditLimit()) {
                        return Mono.error(new IllegalArgumentException("You can't go over the credit limit"));
                    }
                    Mono<Transaction> transactionMono = TransactionConverter.toTransactionExpense(expenseRequest, TransactionType.PAYMENT, "paymen to credit card");
                    creditCard.setAvailableBalance(newBalance);
                    return updateTransaction(transactionMono, creditCard)
                            .then(creditCardRepository.save(creditCard))
                            .then(Mono.just(ExpenseConverter.convertToResponse(expenseRequest)));
                })
                .doOnError(e -> log.error("Error paymen credit card", e))
                .onErrorMap(e -> new Exception("Error paymen credit card", e));
    }

    @Override
    @CircuitBreaker(name = "credit-card", fallbackMethod = "fallbackGetBalanceByClientId")
    @TimeLimiter(name = "credit-card")
    public Flux<BalanceResponse> getBalanceByClientId(String idClient) {
        return creditCardRepository.findByClientId(idClient)
                .map(creditCard -> {
                    if (creditCard == null) {
                        throw new CreditNotFoundException("Credit card not found with id: " + idClient);
                    }
                    return BalanceConverter.toBalanceResponse(Collections.singletonList(creditCard));
                })
                .switchIfEmpty(Mono.error(new CreditNotFoundException("Account not found with id: " + idClient)))
                .doOnError(e -> log.error("Error getting balance for Credit card", e))
                .onErrorMap(e -> new Exception("Error getting balance for Credit card", e));
    }
    @Override
    @CircuitBreaker(name = "credit-card", fallbackMethod = "fallbackGetTransactionByCreditCard")
    @TimeLimiter(name = "credit-card")
    public Mono<TransactionCreditCardResponse> getTransactionByCreditCard(String idAccount) {
        return creditCardRepository.findById(idAccount).map(TransactionConverter::toTransactionAccountResponse)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("account not found with id: " + idAccount)))
                .doOnError(e -> log.error("Error fetching Credit card with id: {}", idAccount, e))
                .onErrorMap(e -> new Exception("Error fetching Credit card by id", e));
    }

    @Override
    @CircuitBreaker(name = "credit-card", fallbackMethod = "fallbackGetAllCreditCardByClientId")
    @TimeLimiter(name = "credit-card")
    public Flux<CreditCardResponse> getAllCreditCardByClientId(String clientId) {
        return creditCardRepository.findByClientId(clientId).map(CreditCardConverter::toCreditCardResponse)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("account not found with client id: " + clientId)))
                .doOnError(e -> log.error("Error fetching Credit card with client id: {}", clientId, e))
                .onErrorMap(e -> new Exception("Error fetching Credit card by client id", e));

    }

    private Mono<TransactionResponse> updateTransaction(Mono<Transaction> transactionMono, CreditCard creditCard) {
        return transactionMono.flatMap(transactionToConverter -> {
            if (creditCard.getTransactions() == null) {
                creditCard.setTransactions(new ArrayList<>());
            }
            creditCard.getTransactions().add(transactionToConverter);
            return TransactionConverter.toTransactionResponse(transactionToConverter);
        });
    }
    public Flux<CreditCardResponse> fallbackGetAllCreditCards(Exception exception) {
        log.error("Fallback method for getAllCreditCards", exception);
        return Flux.error(new Exception("Fallback method for getAllCreditCards"));
    }

    public Mono<CreditCardResponse> fallbackGetCreditCardById(Exception exception) {
        log.error("Fallback method for getCreditCardById", exception);
        return Mono.error(new Exception("Fallback method for getCreditCardById"));
    }

    public Mono<CreditCardResponse> fallbackCreateNewCreditCard(Exception exception) {
        log.error("Fallback method for createNewCreditCard", exception);
        return Mono.error(new Exception("Fallback method for createNewCreditCard"));
    }

    public Mono<CreditCardResponse> fallbackUpdateCreditCard(Exception exception) {
        log.error("Fallback method for updateCreditCard", exception);
        return Mono.error(new Exception("Fallback method for updateCreditCard"));
    }

    public Mono<Void> fallbackDeleteCreditCard(Exception exception) {
        log.error("Fallback method for deleteCreditCard", exception);
        return Mono.error(new Exception("Fallback method for deleteCreditCard"));
    }

    public Mono<ExpenseResponse> fallbackChargeByCardId(Exception exception) {
        log.error("Fallback method for chargeByCardId", exception);
        return Mono.error(new Exception("Fallback method for chargeByCardId"));
    }

    public Mono<ExpenseResponse> fallbackPaymentByCardId(Exception exception) {
        log.error("Fallback method for paymentByCardId", exception);
        return Mono.error(new Exception("Fallback method for paymentByCardId"));
    }

    public Flux<BalanceResponse> fallbackGetBalanceByClientId(Exception exception) {
        log.error("Fallback method for getBalanceByClientId", exception);
        return Flux.error(new Exception("Fallback method for getBalanceByClientId"));
    }

    public Flux<CreditCardResponse> fallbackGetAllCreditCardByClientId(Exception exception) {
        log.error("Fallback method for getAllCreditCardByClientId", exception);
        return Flux.error(new Exception("Fallback method for getAllCreditCardByClientId"));
    }

    public Mono<TransactionCreditCardResponse> fallbackGetTransactionByCreditCard(Exception exception) {
        log.error("Fallback method for getTransactionByCreditCard", exception);
        return Mono.error(new Exception("Fallback method for getTransactionByCreditCard"));
    }
}
