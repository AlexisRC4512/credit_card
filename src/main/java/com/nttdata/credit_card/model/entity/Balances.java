package com.nttdata.credit_card.model.entity;


import lombok.*;

import java.util.Date;
/**
 * Represents the balances of a client.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Balances {
    /**
     * Client ID.
     */
    private String clientId;

    /**
     * Credit balance of the client.
     */
    private double creditBalance;

    /**
     * Date of the balance.
     */
    private Date date;
}
