package com.nttdata.credit_card.controller;

import com.nttdata.credit_card.model.request.CreditCardRequest;
import com.nttdata.credit_card.model.request.ExpenseRequest;
import com.nttdata.credit_card.model.response.BalanceResponse;
import com.nttdata.credit_card.model.response.CreditCardResponse;
import com.nttdata.credit_card.model.response.ExpenseResponse;
import com.nttdata.credit_card.model.response.TransactionCreditCardResponse;
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
    public Mono<CreditCardResponse> getCreditById(@PathVariable String idCard) {
        return creditCardService.getCreditCardById(idCard);
    }

    @PostMapping
    public Mono<CreditCardResponse> createCredit(@RequestBody CreditCardRequest creditCardRequest) {
        return creditCardService.createNewCreditCard(creditCardRequest);
    }

    @PutMapping("/{id_card}")
    public Mono<CreditCardResponse> updateCredit(@PathVariable String idCard, @RequestBody CreditCardRequest creditCardRequest) {
        return creditCardService.updateCreditCard(idCard, creditCardRequest);
    }

    @DeleteMapping("/{id_card}")
    public Mono<Void> deleteCredit(@PathVariable String idCard) {
        return creditCardService.deleteCreditCard(idCard);
    }

    @PostMapping("/{id_card}/charge")
    public Mono<ExpenseResponse> createCredit(@PathVariable String idCard,@RequestBody ExpenseRequest expenseRequest) {
        return creditCardService.chargeByCardId(idCard,expenseRequest);
    }
    @PostMapping("/{id_card}/payment")
    public Mono<ExpenseResponse> createPayment(@PathVariable String idCard,@RequestBody ExpenseRequest expenseRequest) {
        return creditCardService.paymentByCardId(idCard,expenseRequest);
    }
    @GetMapping("/{id_client}/balances")
    public Flux<BalanceResponse> getBalanceAccount(@PathVariable("id_client") String idClient) {
        return creditCardService.getBalanceByClientId(idClient);
    }
    @GetMapping("/{id_account}/transactions")
    public Mono<TransactionCreditCardResponse> getTransactionByAccountId(@PathVariable("id_account") String id) {
        return creditCardService.getTransactionByCreditCard(id);
    }
    @GetMapping("/{id_client}/client")
    public Flux<CreditCardResponse> getAllCreditCardByClientId(@PathVariable("id_client") String idClient) {
        return creditCardService.getAllCreditCardByClientId(idClient);
    }
}
