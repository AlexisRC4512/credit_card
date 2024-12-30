package com.nttdata.credit_card.model.response;


import com.nttdata.credit_card.model.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreditCardResponse {
    private List<Transaction> transactions;
}
