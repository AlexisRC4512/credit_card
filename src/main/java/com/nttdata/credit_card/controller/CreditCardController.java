package com.nttdata.credit_card.controller;

import com.nttdata.credit_card.model.request.CreditCardRequest;
import com.nttdata.credit_card.model.response.CreditCardResponse;
import com.nttdata.credit_card.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/CreditCard")
public class CreditCardController {
    @Autowired
    CreditCardService creditCardService;
    @GetMapping
    public Flux<CreditCardResponse> getAllCreditCards() {
        return creditCardService.getAllCreditCards();
    }

    @GetMapping("/{id_card}")
    public Mono<CreditCardResponse> getCreditById(@PathVariable String id_card) {
        return creditCardService.getCreditCardById(id_card);
    }

    @PostMapping
    public Mono<CreditCardResponse> createCredit(@RequestBody CreditCardRequest creditCardRequest) {
        return creditCardService.createNewCreditCard(creditCardRequest);
    }

    @PutMapping("/{id_card}")
    public Mono<CreditCardResponse> updateCredit(@PathVariable String id_card, @RequestBody CreditCardRequest creditCardRequest) {
        return creditCardService.updateCreditCard(id_card, creditCardRequest);
    }

    @DeleteMapping("/{id_card}")
    public Mono<Void> deleteCredit(@PathVariable String id_card) {
        return creditCardService.deleteCreditCard(id_card);
    }
}
