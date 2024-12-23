package com.nttdata.credit_card.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCreditDataException extends RuntimeException {
    public InvalidCreditDataException(String message) {
        super(message);
    }
}