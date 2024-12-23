package com.nttdata.credit_card.model.entity;

import com.nttdata.credit_card.model.enums.TransactionType;
import lombok.*;

import java.util.Date;
/**
 * Represents a transaction in the system.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {
    /**
     * Client ID associated with the transaction.
     */
    private String clientId;

    /**
     * Type of transaction.
     */
    private TransactionType type;

    /**
     * Amount of the transaction.
     */
    private double amount;

    /**
     * Date of the transaction.
     */
    private Date date;

    /**
     * Description of the transaction.
     */
    private String description;
}