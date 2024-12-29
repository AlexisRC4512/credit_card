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

    @PostMapping("/{id_card}/charge")
    public Mono<ExpenseResponse> createCredit(@PathVariable String id_card,@RequestBody ExpenseRequest expenseRequest) {
        return creditCardService.chargeByCardId(id_card,expenseRequest);
    }
    @PostMapping("/{id_card}/payment")
    public Mono<ExpenseResponse> createPayment(@PathVariable String id_card,@RequestBody ExpenseRequest expenseRequest) {
        return creditCardService.paymentByCardId(id_card,expenseRequest);
    }
    @GetMapping("/{id_client}/balances")
    public Flux<BalanceResponse> getBalanceAccount(@PathVariable("id_client") String idClient){
        return creditCardService.getBalanceByClientId(idClient);
    }
    @GetMapping("/{id_account}/transactions")
    public Mono<TransactionCreditCardResponse> getTransactionByAccountId(@PathVariable("id_account") String id) {
        return creditCardService.getTransactionByCreditCard(id);

    }
}
