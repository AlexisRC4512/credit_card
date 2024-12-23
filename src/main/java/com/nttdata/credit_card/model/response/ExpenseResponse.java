package com.nttdata.credit_card.model.response;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseResponse {
    private String clientId;
    private double amount;
    private Date date;
}
