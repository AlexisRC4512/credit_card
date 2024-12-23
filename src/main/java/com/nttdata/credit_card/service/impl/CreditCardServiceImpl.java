package com.nttdata.credit_card.service.impl;

import com.nttdata.credit_card.model.entity.CreditCard;
import com.nttdata.credit_card.model.exception.CreditNotFoundException;
import com.nttdata.credit_card.model.exception.InvalidCreditDataException;
import com.nttdata.credit_card.model.request.CreditCardRequest;
import com.nttdata.credit_card.model.response.CreditCardResponse;
import com.nttdata.credit_card.service.CreditCardService;
import com.nttdata.credit_card.repository.CreditCardRepository;
import com.nttdata.credit_card.util.CreditCardConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<CreditCardResponse> createNewCreditCard(CreditCardRequest creditCardRequest) {
        if (creditCardRequest == null ) {
            log.warn("Invalid Credit cards data: {}", creditCardRequest);
            return Mono.error(new InvalidCreditDataException("Invalid Credit cards data"));
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
    public Mono<Void> deleteCreditCard(String id) {
        log.debug("Deleting Credit cards with id: {}", id);
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("Credit cards not found with id: " + id)))
                .flatMap(existingClient -> creditCardRepository.delete(existingClient))
                .onErrorMap(e -> new Exception("Error deleting Credit cards", e));
    }
}
