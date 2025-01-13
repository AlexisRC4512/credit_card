package com.nttdata.credit_card.service;

import com.nttdata.credit_card.model.entity.CreditCard;
import com.nttdata.credit_card.model.enums.TypeCredit;
import com.nttdata.credit_card.model.exception.InvalidCreditDataException;
import com.nttdata.credit_card.model.request.CreditCardRequest;
import com.nttdata.credit_card.model.request.ExpenseRequest;
import com.nttdata.credit_card.model.response.ExpenseResponse;
import com.nttdata.credit_card.repository.CreditCardRepository;
import com.nttdata.credit_card.service.impl.CreditCardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class CreditCardServiceTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardServiceImpl creditCardService;

    private CreditCard creditCard;
    private CreditCardRequest creditCardRequest;

    @BeforeEach
    void setUp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2026);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date expirationDate = calendar.getTime();
        creditCard = new CreditCard("1", TypeCredit.PERSONAL,982392029, 10000.0, 10000.0, new Date(), expirationDate, "12345", List.of());
        creditCardRequest = new CreditCardRequest(TypeCredit.PERSONAL, 10000.0, 10000.0, new Date(), expirationDate, "12345",List.of(),982392029);
    }

    @Test
    void getAllCreditCardsSuccess() {
        when(creditCardRepository.findAll()).thenReturn(Flux.just(creditCard));

        StepVerifier.create(creditCardService.getAllCreditCards())
                .expectNextMatches(creditCardResponse -> creditCardResponse != null)
                .verifyComplete();

        verify(creditCardRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllCreditCardsError() {
        when(creditCardRepository.findAll()).thenReturn(Flux.error(new RuntimeException("Database error")));

        StepVerifier.create(creditCardService.getAllCreditCards())
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("Error fetching all Credit cards"))
                .verify();

        verify(creditCardRepository, times(1)).findAll();
    }

    @Test
    public void testGetCreditCardByIdSuccess() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.just(creditCard));

        StepVerifier.create(creditCardService.getCreditCardById("1"))
                .expectNextMatches(creditcardResponse -> creditcardResponse != null)
                .verifyComplete();

        verify(creditCardRepository, times(1)).findById("1");
    }

    @Test
    public void testGetCreditCardByIdNotFound() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(creditCardService.getCreditCardById("1"))
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("Error fetching Credit cards by id"))
                .verify();

        verify(creditCardRepository, times(1)).findById("1");
    }

    @Test
    public void testCreateNewCreditCardSuccess() {
        when(creditCardRepository.save(any(CreditCard.class))).thenReturn(Mono.just(creditCard));

        StepVerifier.create(creditCardService.createNewCreditCard(creditCardRequest))
                .expectNextMatches(creditcardResponse -> creditcardResponse != null)
                .verifyComplete();

        verify(creditCardRepository, times(1)).save(any(CreditCard.class));
    }

    @Test
    public void testCreateNewCreditCardInvalidData() {
        StepVerifier.create(creditCardService.createNewCreditCard(null))
                .expectErrorMatches(throwable -> throwable instanceof InvalidCreditDataException && throwable.getMessage().equals("Invalid Credit cards data"))
                .verify();

        verify(creditCardRepository, times(0)).save(any(CreditCard.class));
    }

    @Test
    public void testUpdateCreditCardSuccess() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.just(creditCard));
        when(creditCardRepository.save(any(CreditCard.class))).thenReturn(Mono.just(creditCard));

        StepVerifier.create(creditCardService.updateCreditCard("1", creditCardRequest))
                .expectNextMatches(creditcardResponse -> creditcardResponse != null)
                .verifyComplete();

        verify(creditCardRepository, times(1)).findById("1");
        verify(creditCardRepository, times(1)).save(any(CreditCard.class));
    }

    @Test
    public void testUpdateCreditCardNotFound() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(creditCardService.updateCreditCard("1", creditCardRequest))
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("Error updating Credit cards"))
                .verify();

        verify(creditCardRepository, times(1)).findById("1");
        verify(creditCardRepository, times(0)).save(any(CreditCard.class));
    }

    @Test
    public void testDeleteCreditCardSuccess() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.just(creditCard));
        when(creditCardRepository.delete(any(CreditCard.class))).thenReturn(Mono.empty());

        StepVerifier.create(creditCardService.deleteCreditCard("1"))
                .verifyComplete();

        verify(creditCardRepository, times(1)).findById("1");
        verify(creditCardRepository, times(1)).delete(any(CreditCard.class));
    }

    @Test
    public void testDeleteCreditCardNotFound() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(creditCardService.deleteCreditCard("1"))
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("Error deleting Credit cards"))
                .verify();

        verify(creditCardRepository, times(1)).findById("1");
        verify(creditCardRepository, times(0)).delete(any(CreditCard.class));
    }
    @Test
    void testChargeByCardIdSuccess() {
        String cardId = "123";
        ExpenseRequest expenseRequest = new ExpenseRequest("client1", 100.0, "Test charge");
        CreditCard creditCard = new CreditCard();
        creditCard.setAvailableBalance(200.0);

        when(creditCardRepository.findById(cardId)).thenReturn(Mono.just(creditCard));
        when(creditCardRepository.save(any(CreditCard.class))).thenReturn(Mono.just(creditCard));

        Mono<ExpenseResponse> result = creditCardService.chargeByCardId(cardId, expenseRequest);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getAmount() == 100.0)
                .verifyComplete();

        verify(creditCardRepository).findById(cardId);
        verify(creditCardRepository).save(any(CreditCard.class));
    }

    @Test
    void testChargeByCardIdCreditNotFound() {
        String cardId = "123";
        ExpenseRequest expenseRequest = new ExpenseRequest("client1", 100.0, "Test charge");

        when(creditCardRepository.findById(cardId)).thenReturn(Mono.empty());

        Mono<ExpenseResponse> result = creditCardService.chargeByCardId(cardId, expenseRequest);

        StepVerifier.create(result)
                .expectError(Exception.class)
                .verify();

        verify(creditCardRepository).findById(cardId);
    }

    @Test
    void testChargeByCardIdInsufficientBalance() {
        String cardId = "123";
        ExpenseRequest expenseRequest = new ExpenseRequest("client1", 300.0, "Test charge");
        CreditCard creditCard = new CreditCard();
        creditCard.setAvailableBalance(200.0);

        when(creditCardRepository.findById(cardId)).thenReturn(Mono.just(creditCard));

        Mono<ExpenseResponse> result = creditCardService.chargeByCardId(cardId, expenseRequest);

        StepVerifier.create(result)
                .expectError(Exception.class)
                .verify();

        verify(creditCardRepository).findById(cardId);
    }
    @Test
    void testPaymentByCardIdSuccess() {
        String cardId = "123";
        ExpenseRequest expenseRequest = new ExpenseRequest("client1", 100.0, "Test payment");
        CreditCard creditCard = new CreditCard();
        creditCard.setAvailableBalance(200.0);
        creditCard.setCreditLimit(500.0);

        when(creditCardRepository.findById(cardId)).thenReturn(Mono.just(creditCard));
        when(creditCardRepository.save(any(CreditCard.class))).thenReturn(Mono.just(creditCard));

        Mono<ExpenseResponse> result = creditCardService.paymentByCardId(cardId, expenseRequest);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getAmount() == 100.0)
                .verifyComplete();

        verify(creditCardRepository).findById(cardId);
        verify(creditCardRepository).save(any(CreditCard.class));
    }

    @Test
    void testPaymentByCardIdCreditNotFound() {
        String cardId = "123";
        ExpenseRequest expenseRequest = new ExpenseRequest("client1", 100.0, "Test payment");

        when(creditCardRepository.findById(cardId)).thenReturn(Mono.empty());

        Mono<ExpenseResponse> result = creditCardService.paymentByCardId(cardId, expenseRequest);

        StepVerifier.create(result)
                .expectError(Exception.class)
                .verify();

        verify(creditCardRepository).findById(cardId);
    }

    @Test
    void testPaymentByCardIdExceedsCreditLimit() {
        String cardId = "123";
        ExpenseRequest expenseRequest = new ExpenseRequest("client1", 400.0, "Test payment");
        CreditCard creditCard = new CreditCard();
        creditCard.setAvailableBalance(200.0);
        creditCard.setCreditLimit(500.0);

        when(creditCardRepository.findById(cardId)).thenReturn(Mono.just(creditCard));

        Mono<ExpenseResponse> result = creditCardService.paymentByCardId(cardId, expenseRequest);

        StepVerifier.create(result)
                .expectError(Exception.class)
                .verify();

        verify(creditCardRepository).findById(cardId);
    }
}
