package com.nttdata.credit_card.model.response;

import com.nttdata.credit_card.model.entity.Balances;
import com.nttdata.credit_card.model.entity.Transaction;
import com.nttdata.credit_card.model.enums.TypeCredit;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreditCardResponse {
    private String id;
    private TypeCredit type;
    private double creditLimit;
    private double availableBalance;
    private Date issueDate;
    private Date expirationDate;
    private String clientId;
    private List<Transaction> transactions;
    private Balances balances;
}
