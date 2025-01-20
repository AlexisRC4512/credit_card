package com.nttdata.credit_card.controller;

import com.nttdata.credit_card.api.ApiApi;
import com.nttdata.credit_card.model.request.CreditCardRequest;
import com.nttdata.credit_card.model.request.ExpenseRequest;
import com.nttdata.credit_card.model.response.BalanceResponse;
import com.nttdata.credit_card.model.response.CreditCardResponse;
import com.nttdata.credit_card.model.response.ExpenseResponse;
import com.nttdata.credit_card.model.response.TransactionCreditCardResponse;
import com.nttdata.credit_card.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/CreditCard")
@RequiredArgsConstructor
public class CreditCardController implements ApiApi {
    private final CreditCardService creditCardService;
    @GetMapping
    public Flux<CreditCardResponse> getAllCreditCards() {
        return creditCardService.getAllCreditCards();
    }

    @GetMapping("/{idCard}")
    public Mono<CreditCardResponse> getCreditById(@PathVariable String idCard) {
        return creditCardService.getCreditCardById(idCard);
    }

    @PostMapping
    public Mono<CreditCardResponse> createCredit(@RequestBody CreditCardRequest creditCardRequest) {
        return creditCardService.createNewCreditCard(creditCardRequest);
    }

    @PutMapping("/{idCard}")
    public Mono<CreditCardResponse> updateCredit(@PathVariable String idCard, @RequestBody CreditCardRequest creditCardRequest) {
        return creditCardService.updateCreditCard(idCard, creditCardRequest);
    }

    @DeleteMapping("/{idCard}")
    public Mono<Void> deleteCredit(@PathVariable String idCard) {
        return creditCardService.deleteCreditCard(idCard);
    }

    @PostMapping("/{idCard}/charge")
    public Mono<ExpenseResponse> createCredit(@PathVariable String idCard,@RequestBody ExpenseRequest expenseRequest) {
        return creditCardService.chargeByCardId(idCard,expenseRequest);
    }
    @PostMapping("/{idCard}/payment")
    public Mono<ExpenseResponse> createPayment(@PathVariable("idCard") String idCard,@RequestBody ExpenseRequest expenseRequest) {
        return creditCardService.paymentByCardId(idCard,expenseRequest);
    }
    @GetMapping("/{idCard}/balances")
    public Flux<BalanceResponse> getBalanceAccount(@PathVariable("idCard") String idClient) {
        return creditCardService.getBalanceByClientId(idClient);
    }
    @GetMapping("/{idAccount}/transactions")
    public Mono<TransactionCreditCardResponse> getTransactionByAccountId(@PathVariable("idAccount") String id) {
        return creditCardService.getTransactionByCreditCard(id);
    }
    @GetMapping("/{idClient}/client")
    public Flux<CreditCardResponse> getAllCreditCardByClientId(@PathVariable("idClient") String idClient) {
        return creditCardService.getAllCreditCardByClientId(idClient);
    }
}
