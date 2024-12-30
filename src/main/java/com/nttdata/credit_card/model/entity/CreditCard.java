package com.nttdata.credit_card.model.entity;

import com.nttdata.credit_card.model.enums.TypeCredit;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Represents a credit card in the system.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "credit_card")
public class CreditCard {

    /**
     * Unique identifier for the credit card.
     */
    @Id
    private String id;

    /**
     * Type of the credit card.
     */
    private TypeCredit type;

    /**
     * Credit limit of the credit card.
     */
    private double creditLimit;

    /**
     * Available balance on the credit card.
     */
    private double availableBalance;

    /**
     * Issue date of the credit card.
     */
    private Date issueDate;

    /**
     * Expiration date of the credit card.
     */
    private Date expirationDate;

    /**
     * Client ID associated with the credit card.
     */
    private String clientId;

    /**
     * List of transactions associated with the credit card.
     */
    private List<Transaction> transactions;

}
