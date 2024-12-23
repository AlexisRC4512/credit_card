package com.nttdata.credit_card.model.response;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BalanceResponse {
    private String clientId;
    private double creditCardBalance;
}
