package com.nttdata.credit_card.service;

import com.nttdata.credit_card.model.entity.Balances;
import com.nttdata.credit_card.model.entity.CreditCard;
import com.nttdata.credit_card.model.entity.Transaction;
import com.nttdata.credit_card.model.enums.TransactionType;
import com.nttdata.credit_card.model.enums.TypeCredit;
import com.nttdata.credit_card.model.exception.CreditNotFoundException;
import com.nttdata.credit_card.model.exception.InvalidCreditDataException;
import com.nttdata.credit_card.model.request.CreditCardRequest;
import com.nttdata.credit_card.repository.CreditCardRepository;
import com.nttdata.credit_card.service.impl.CreditCardServiceImpl;
import com.nttdata.credit_card.util.CreditCardConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
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
        creditCard = new CreditCard("1", TypeCredit.PERSONAL, 10000.0, 5000.0, new Date(), expirationDate, "12345", List.of(), new Balances());
        creditCardRequest = new CreditCardRequest(TypeCredit.PERSONAL, 10000.0, 5000.0, new Date(), expirationDate, "12345",List.of(), new Balances());
    }

    @Test
    void getAllCreditCards_success() {
        when(creditCardRepository.findAll()).thenReturn(Flux.just(creditCard));

        StepVerifier.create(creditCardService.getAllCreditCards())
                .expectNextMatches(creditCardResponse -> creditCardResponse != null)
                .verifyComplete();

        verify(creditCardRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllCreditCards_Error() {
        when(creditCardRepository.findAll()).thenReturn(Flux.error(new RuntimeException("Database error")));

        StepVerifier.create(creditCardService.getAllCreditCards())
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("Error fetching all Credit cards"))
                .verify();

        verify(creditCardRepository, times(1)).findAll();
    }

    @Test
    public void testGetCreditCardById_Success() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.just(creditCard));

        StepVerifier.create(creditCardService.getCreditCardById("1"))
                .expectNextMatches(creditcardResponse -> creditcardResponse!=null)
                .verifyComplete();

        verify(creditCardRepository, times(1)).findById("1");
    }

    @Test
    public void testGetCreditCardById_NotFound() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(creditCardService.getCreditCardById("1"))
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("Error fetching Credit cards by id"))
                .verify();

        verify(creditCardRepository, times(1)).findById("1");
    }

    @Test
    public void testCreateNewCreditCard_Success() {
        when(creditCardRepository.save(any(CreditCard.class))).thenReturn(Mono.just(creditCard));

        StepVerifier.create(creditCardService.createNewCreditCard(creditCardRequest))
                .expectNextMatches(creditcardResponse -> creditcardResponse!=null)
                .verifyComplete();

        verify(creditCardRepository, times(1)).save(any(CreditCard.class));
    }

    @Test
    public void testCreateNewCreditCard_InvalidData() {
        StepVerifier.create(creditCardService.createNewCreditCard(null))
                .expectErrorMatches(throwable -> throwable instanceof InvalidCreditDataException && throwable.getMessage().equals("Invalid Credit cards data"))
                .verify();

        verify(creditCardRepository, times(0)).save(any(CreditCard.class));
    }

    @Test
    public void testUpdateCreditCard_Success() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.just(creditCard));
        when(creditCardRepository.save(any(CreditCard.class))).thenReturn(Mono.just(creditCard));

        StepVerifier.create(creditCardService.updateCreditCard("1", creditCardRequest))
                .expectNextMatches(creditcardResponse -> creditcardResponse!=null)
                .verifyComplete();

        verify(creditCardRepository, times(1)).findById("1");
        verify(creditCardRepository, times(1)).save(any(CreditCard.class));
    }

    @Test
    public void testUpdateCreditCard_NotFound() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(creditCardService.updateCreditCard("1", creditCardRequest))
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("Error updating Credit cards"))
                .verify();

        verify(creditCardRepository, times(1)).findById("1");
        verify(creditCardRepository, times(0)).save(any(CreditCard.class));
    }

    @Test
    public void testDeleteCreditCard_Success() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.just(creditCard));
        when(creditCardRepository.delete(any(CreditCard.class))).thenReturn(Mono.empty());

        StepVerifier.create(creditCardService.deleteCreditCard("1"))
                .verifyComplete();

        verify(creditCardRepository, times(1)).findById("1");
        verify(creditCardRepository, times(1)).delete(any(CreditCard.class));
    }

    @Test
    public void testDeleteCreditCard_NotFound() {
        when(creditCardRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(creditCardService.deleteCreditCard("1"))
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("Error deleting Credit cards"))
                .verify();

        verify(creditCardRepository, times(1)).findById("1");
        verify(creditCardRepository, times(0)).delete(any(CreditCard.class));
    }
}
